-- Net32 Dental Supply — database schema
-- member table: base kitchensink entity + Net32 tier augmentation

CREATE TABLE IF NOT EXISTS member (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(25)   NOT NULL,
    email           VARCHAR(255)  NOT NULL UNIQUE,
    phone_number    VARCHAR(12)   NOT NULL,
    tier            VARCHAR(20)   NOT NULL DEFAULT 'BRONZE',
    total_spend     NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    tier_updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    sku         VARCHAR(100)   NOT NULL UNIQUE,
    base_price  NUMERIC(12,4)  NOT NULL,
    weight_lbs  NUMERIC(8,4),
    category    VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS vendors (
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255)  NOT NULL,
    fulfillment_rating NUMERIC(3,1),
    avg_shipping_days  INTEGER,
    contact_email      VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS vendor_inventory (
    vendor_id          BIGINT        NOT NULL REFERENCES vendors(id),
    product_id         BIGINT        NOT NULL REFERENCES products(id),
    markup_percent     NUMERIC(6,2),
    quantity_available INTEGER,
    PRIMARY KEY (vendor_id, product_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id              BIGSERIAL PRIMARY KEY,
    member_id       BIGINT        NOT NULL REFERENCES member(id),
    status          VARCHAR(50)   NOT NULL,
    subtotal        NUMERIC(12,2),
    discount_amount NUMERIC(12,2),
    shipping_cost   NUMERIC(8,2),
    total           NUMERIC(12,2),
    created_at      TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT        REFERENCES orders(id),
    product_id BIGINT        REFERENCES products(id),
    vendor_id  BIGINT        REFERENCES vendors(id),
    quantity   INTEGER,
    unit_price NUMERIC(12,4),
    line_total NUMERIC(12,2)
);

CREATE TABLE IF NOT EXISTS shipping_zones (
    id                BIGSERIAL PRIMARY KEY,
    zone_name         VARCHAR(100),
    zip_range_start   VARCHAR(5),
    zip_range_end     VARCHAR(5),
    base_rate_per_lb  NUMERIC(6,4),
    min_days          INTEGER,
    max_days          INTEGER
);

CREATE TABLE IF NOT EXISTS discount_audit (
    id           BIGSERIAL PRIMARY KEY,
    member_id    BIGINT        REFERENCES member(id),
    base_total   NUMERIC(12,2),
    discount_pct NUMERIC(5,4),
    discount_amt NUMERIC(12,2),
    applied_at   TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_draft_items (
    id         BIGSERIAL PRIMARY KEY,
    member_id  BIGINT   REFERENCES member(id),
    product_id BIGINT   REFERENCES products(id),
    quantity   INTEGER
);
