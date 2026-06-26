-- Stored procedure call graph (key complexity for migration):
--
--   process_order()
--     -> select_best_vendor()          [per line item]
--          -> calculate_price()        [for each candidate vendor, inline in ORDER BY scoring]
--     -> calculate_price()             [again for chosen vendor to get final unit price]
--     -> apply_customer_discount()     [on subtotal]
--     -> calculate_shipping()          [on total weight]
--
-- Java service call graph mirrors this in OrderService.previewOrder():
--   OrderService
--     -> VendorSelectionService.selectBestVendor()
--     -> PricingService.calculatePrice()             [SHARED dep]
--     -> DiscountService.calculateDiscount()
--          -> PricingService (injected into DiscountService as shared dep)
--     -> ShippingService.estimateShipping()

-- =============================================================================
-- 1. calculate_price
-- =============================================================================
CREATE OR REPLACE FUNCTION calculate_price(
    p_product_id BIGINT,
    p_vendor_id  BIGINT,
    p_quantity   INT
)
RETURNS NUMERIC AS $$
DECLARE
    v_base_price  NUMERIC;
    v_markup_pct  NUMERIC;
    v_volume_disc NUMERIC := 0;
    v_unit_price  NUMERIC;
BEGIN
    SELECT p.base_price, vi.markup_percent
    INTO v_base_price, v_markup_pct
    FROM products p
    JOIN vendor_inventory vi ON vi.product_id = p.id
    WHERE p.id = p_product_id AND vi.vendor_id = p_vendor_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'No vendor inventory found for product % vendor %',
            p_product_id, p_vendor_id
            USING ERRCODE = 'P0001';
    END IF;

    IF p_quantity >= 100 THEN
        v_volume_disc := 0.15;
    ELSIF p_quantity >= 50 THEN
        v_volume_disc := 0.10;
    ELSIF p_quantity >= 20 THEN
        v_volume_disc := 0.05;
    ELSIF p_quantity >= 10 THEN
        v_volume_disc := 0.02;
    END IF;

    v_unit_price := ROUND(
        v_base_price * (1 + v_markup_pct / 100.0) * (1 - v_volume_disc),
        4
    );
    RETURN v_unit_price;
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- 2. select_best_vendor
-- =============================================================================
CREATE OR REPLACE FUNCTION select_best_vendor(
    p_product_id BIGINT,
    p_quantity   INT
)
RETURNS BIGINT AS $$
DECLARE
    v_vendor_id BIGINT;
    v_min_price NUMERIC;
BEGIN
    -- Get minimum price across all eligible vendors for normalization
    SELECT MIN(calculate_price(p_product_id, vi.vendor_id, p_quantity))
    INTO v_min_price
    FROM vendor_inventory vi
    WHERE vi.product_id = p_product_id
      AND vi.quantity_available >= p_quantity;

    IF v_min_price IS NOT NULL THEN
        -- Score vendors: price 60%, fulfillment 30%, shipping days 10%
        SELECT vi.vendor_id INTO v_vendor_id
        FROM vendor_inventory vi
        JOIN vendors v ON v.id = vi.vendor_id
        WHERE vi.product_id = p_product_id
          AND vi.quantity_available >= p_quantity
        ORDER BY
            (calculate_price(p_product_id, vi.vendor_id, p_quantity) / v_min_price) * 0.6
            + (1.0 - v.fulfillment_rating / 5.0) * 0.3
            + (v.avg_shipping_days / 10.0) * 0.1
        LIMIT 1;
    ELSE
        -- Fallback: cheapest vendor with any stock
        SELECT vi.vendor_id INTO v_vendor_id
        FROM vendor_inventory vi
        WHERE vi.product_id = p_product_id
          AND vi.quantity_available > 0
        ORDER BY calculate_price(p_product_id, vi.vendor_id, p_quantity)
        LIMIT 1;
    END IF;

    RETURN v_vendor_id;
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- 3. apply_customer_discount
-- =============================================================================
CREATE OR REPLACE FUNCTION apply_customer_discount(
    p_member_id  BIGINT,
    p_base_total NUMERIC
)
RETURNS NUMERIC AS $$
DECLARE
    v_tier         VARCHAR(20);
    v_discount_pct NUMERIC;
    v_discount_amt NUMERIC;
BEGIN
    SELECT tier INTO v_tier
    FROM member
    WHERE id = p_member_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Member not found: %', p_member_id
            USING ERRCODE = 'P0002';
    END IF;

    -- Tier discount rates: BRONZE=2%, SILVER=5%, GOLD=8%, PLATINUM=12%
    CASE v_tier
        WHEN 'PLATINUM' THEN v_discount_pct := 0.12;
        WHEN 'GOLD'     THEN v_discount_pct := 0.08;
        WHEN 'SILVER'   THEN v_discount_pct := 0.05;
        ELSE                 v_discount_pct := 0.02;  -- BRONZE default
    END CASE;

    v_discount_amt := ROUND(p_base_total * v_discount_pct, 2);

    -- Insert audit row
    INSERT INTO discount_audit (member_id, base_total, discount_pct, discount_amt, applied_at)
    VALUES (p_member_id, p_base_total, v_discount_pct, v_discount_amt, NOW());

    RETURN v_discount_amt;
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- 4. calculate_shipping
-- =============================================================================
CREATE OR REPLACE FUNCTION calculate_shipping(
    p_destination_zip  VARCHAR,
    p_total_weight_lbs NUMERIC,
    p_expedite         BOOLEAN
)
RETURNS NUMERIC AS $$
DECLARE
    v_zip_prefix     INTEGER;
    v_base_rate      NUMERIC;
    v_shipping_cost  NUMERIC;
BEGIN
    -- Determine zone from ZIP prefix (first 3 digits)
    BEGIN
        v_zip_prefix := SUBSTRING(p_destination_zip FROM 1 FOR 3)::INTEGER;
    EXCEPTION WHEN OTHERS THEN
        v_zip_prefix := 0;  -- Fallback for malformed ZIP
    END;

    SELECT base_rate_per_lb INTO v_base_rate
    FROM shipping_zones
    WHERE SUBSTRING(zip_range_start FROM 1 FOR 3)::INTEGER <= v_zip_prefix
      AND SUBSTRING(zip_range_end   FROM 1 FOR 3)::INTEGER >= v_zip_prefix
    ORDER BY id
    LIMIT 1;

    -- Fallback to a default rate if no zone matched
    IF v_base_rate IS NULL THEN
        v_base_rate := 1.50;
    END IF;

    -- GREATEST(5.99, base_rate * weight)
    v_shipping_cost := GREATEST(5.99, v_base_rate * p_total_weight_lbs);

    -- 2.5x surcharge for expedited shipping
    IF p_expedite THEN
        v_shipping_cost := v_shipping_cost * 2.5;
    END IF;

    RETURN ROUND(v_shipping_cost, 2);
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- 5. process_order
-- =============================================================================
CREATE OR REPLACE FUNCTION process_order(
    p_member_id       BIGINT,
    p_destination_zip VARCHAR,
    p_expedite        BOOLEAN
)
RETURNS BIGINT AS $$
DECLARE
    v_order_id      BIGINT;
    v_subtotal      NUMERIC := 0;
    v_total_weight  NUMERIC := 0;
    v_discount_amt  NUMERIC;
    v_shipping_cost NUMERIC;
    v_total         NUMERIC;
    v_vendor_id     BIGINT;
    v_unit_price    NUMERIC;
    v_line_total    NUMERIC;
    v_member_exists BOOLEAN;
    v_cart_count    INTEGER;
    r_draft         RECORD;
    v_weight        NUMERIC;
BEGIN
    -- Validate member exists
    SELECT EXISTS(SELECT 1 FROM member WHERE id = p_member_id) INTO v_member_exists;
    IF NOT v_member_exists THEN
        RAISE EXCEPTION 'Member not found: %', p_member_id
            USING ERRCODE = 'P0003';
    END IF;

    -- Validate cart is not empty
    SELECT COUNT(*) INTO v_cart_count
    FROM order_draft_items
    WHERE member_id = p_member_id;

    IF v_cart_count = 0 THEN
        RAISE EXCEPTION 'Cart is empty for member %', p_member_id
            USING ERRCODE = 'P0004';
    END IF;

    -- Create the order record
    INSERT INTO orders (member_id, status, subtotal, discount_amount, shipping_cost, total, created_at)
    VALUES (p_member_id, 'CONFIRMED', 0, 0, 0, 0, NOW())
    RETURNING id INTO v_order_id;

    -- Process each draft item
    FOR r_draft IN
        SELECT odi.product_id, odi.quantity, p.weight_lbs
        FROM order_draft_items odi
        JOIN products p ON p.id = odi.product_id
        WHERE odi.member_id = p_member_id
    LOOP
        -- Select best vendor for this item
        v_vendor_id := select_best_vendor(r_draft.product_id, r_draft.quantity);

        -- Calculate unit price for chosen vendor
        v_unit_price := calculate_price(r_draft.product_id, v_vendor_id, r_draft.quantity);
        v_line_total := ROUND(v_unit_price * r_draft.quantity, 2);

        -- Insert order item
        INSERT INTO order_items (order_id, product_id, vendor_id, quantity, unit_price, line_total)
        VALUES (v_order_id, r_draft.product_id, v_vendor_id, r_draft.quantity, v_unit_price, v_line_total);

        v_subtotal     := v_subtotal + v_line_total;
        v_weight       := COALESCE(r_draft.weight_lbs, 0) * r_draft.quantity;
        v_total_weight := v_total_weight + v_weight;
    END LOOP;

    -- Apply customer discount (also writes audit row)
    v_discount_amt := apply_customer_discount(p_member_id, v_subtotal);

    -- Calculate shipping
    v_shipping_cost := calculate_shipping(p_destination_zip, v_total_weight, p_expedite);

    -- Final total
    v_total := v_subtotal - v_discount_amt + v_shipping_cost;

    -- Update order with final amounts
    UPDATE orders
    SET subtotal        = v_subtotal,
        discount_amount = v_discount_amt,
        shipping_cost   = v_shipping_cost,
        total           = v_total
    WHERE id = v_order_id;

    -- Update member total_spend and tier_updated_at
    UPDATE member
    SET total_spend     = total_spend + v_total,
        tier_updated_at = NOW()
    WHERE id = p_member_id;

    -- Clear draft cart
    DELETE FROM order_draft_items WHERE member_id = p_member_id;

    RETURN v_order_id;
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- 6. recalculate_customer_tiers
-- =============================================================================
CREATE OR REPLACE FUNCTION recalculate_customer_tiers()
RETURNS VOID AS $$
DECLARE
    r_member      RECORD;
    v_spend_90d   NUMERIC;
    v_new_tier    VARCHAR(20);
BEGIN
    FOR r_member IN SELECT id FROM member LOOP
        -- Sum order totals from the last 90 days
        SELECT COALESCE(SUM(o.total), 0) INTO v_spend_90d
        FROM orders o
        WHERE o.member_id = r_member.id
          AND o.status = 'CONFIRMED'
          AND o.created_at >= NOW() - INTERVAL '90 days';

        -- Determine new tier based on 90-day spend
        v_new_tier := CASE
            WHEN v_spend_90d >= 5000 THEN 'PLATINUM'
            WHEN v_spend_90d >= 2000 THEN 'GOLD'
            WHEN v_spend_90d >=  500 THEN 'SILVER'
            ELSE                          'BRONZE'
        END;

        -- Update only if tier actually changed
        UPDATE member
        SET tier            = v_new_tier,
            tier_updated_at = NOW()
        WHERE id = r_member.id
          AND tier IS DISTINCT FROM v_new_tier;
    END LOOP;
END;
$$ LANGUAGE plpgsql;
