package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.as.quickstarts.kitchensink.data.OrderRepository;
import org.jboss.as.quickstarts.kitchensink.model.Order;
import org.jboss.as.quickstarts.kitchensink.model.OrderDraftItem;
import org.jboss.as.quickstarts.kitchensink.model.OrderItem;

/**
 * OrderService - orchestrates the full order lifecycle.
 *
 * previewOrder() mirrors the logic of process_order() stored procedure.
 * submitOrder() delegates to process_order() for atomic execution.
 *
 * Shared dependencies: PricingService, DiscountService, VendorSelectionService, ShippingService
 * are all injected here as well as into each other (see DiscountService javadoc for cross-dep note).
 */
@ApplicationScoped
public class OrderService {

    @Inject
    private EntityManager em;

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private PricingService pricingService;

    @Inject
    private VendorSelectionService vendorSelectionService;

    @Inject
    private DiscountService discountService;

    @Inject
    private ShippingService shippingService;

    /**
     * Adds a product to the member's draft cart.
     *
     * @param memberId   the member ID
     * @param productId  the product ID
     * @param quantity   the quantity to add
     */
    @Transactional
    public void addToCart(Long memberId, Long productId, int quantity) {
        OrderDraftItem item = new OrderDraftItem();
        item.setMemberId(memberId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        em.persist(item);
    }

    /**
     * Removes a product from the member's draft cart.
     *
     * @param memberId   the member ID
     * @param productId  the product ID to remove
     */
    @Transactional
    public void removeFromCart(Long memberId, Long productId) {
        em.createQuery(
                "DELETE FROM OrderDraftItem odi WHERE odi.memberId = :memberId AND odi.productId = :productId")
            .setParameter("memberId", memberId)
            .setParameter("productId", productId)
            .executeUpdate();
    }

    /**
     * Previews the order total without committing it.
     *
     * NOTE: This method mirrors the logic of the process_order() stored procedure.
     * Both paths must be kept in sync. Any pricing, discount, or shipping logic change
     * must be applied to BOTH this method AND the stored procedure.
     *
     * @param memberId        the member ID
     * @param destinationZip  the destination ZIP code
     * @param expedite        whether expedited shipping is requested
     * @return                an OrderPreview with full cost breakdown
     */
    public OrderPreview previewOrder(Long memberId, String destinationZip, boolean expedite) {
        List<OrderDraftItem> draftItems = em.createQuery(
                "SELECT odi FROM OrderDraftItem odi WHERE odi.memberId = :memberId",
                OrderDraftItem.class)
            .setParameter("memberId", memberId)
            .getResultList();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        List<LineItemPreview> lineItems = new ArrayList<>();
        Map<Long, Integer> productQuantities = new HashMap<>();

        for (OrderDraftItem draftItem : draftItems) {
            Long productId = draftItem.getProductId();
            int quantity = draftItem.getQuantity();

            Long vendorId = vendorSelectionService.selectBestVendor(productId, quantity);
            if (vendorId == null) {
                continue;
            }

            BigDecimal unitPrice = pricingService.calculatePrice(productId, vendorId, quantity);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            subtotal = subtotal.add(lineTotal);
            productQuantities.put(productId, quantity);

            LineItemPreview lip = new LineItemPreview(productId, vendorId, quantity, unitPrice, lineTotal);
            lineItems.add(lip);
        }

        BigDecimal discountAmount = discountService.calculateDiscount(memberId, subtotal);
        BigDecimal shippingCost = shippingService.estimateShipping(productQuantities, destinationZip, expedite);
        BigDecimal total = subtotal.subtract(discountAmount).add(shippingCost);

        return new OrderPreview(subtotal, discountAmount, shippingCost, total, lineItems);
    }

    /**
     * Submits the order by calling the process_order() stored procedure.
     *
     * Stored procedure: process_order(member_id, destination_zip, expedite)
     * Orchestrates: select_best_vendor -> calculate_price -> apply_customer_discount -> calculate_shipping
     * Clears draft cart and updates member total_spend atomically
     *
     * @param memberId        the member ID
     * @param destinationZip  the destination ZIP code
     * @param expedite        whether expedited shipping is requested
     * @return                the new order ID
     */
    @Transactional
    public Long submitOrder(Long memberId, String destinationZip, boolean expedite) {
        Object result = em.createNativeQuery(
                "SELECT process_order(:memberId, :destinationZip, :expedite)")
            .setParameter("memberId", memberId)
            .setParameter("destinationZip", destinationZip)
            .setParameter("expedite", expedite)
            .getSingleResult();

        if (result instanceof Long) {
            return (Long) result;
        } else if (result instanceof Number) {
            return ((Number) result).longValue();
        } else {
            throw new IllegalStateException(
                "Unexpected result type from process_order: " + result.getClass());
        }
    }

    /**
     * Returns a single order by ID.
     *
     * @param orderId  the order ID
     * @return         the Order entity
     */
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Returns the order history for a member.
     *
     * @param memberId  the member ID
     * @return          list of orders sorted by creation date descending
     */
    public List<Order> getOrderHistory(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    // -----------------------------------------------------------------------
    // Inner classes
    // -----------------------------------------------------------------------

    public static class OrderPreview {
        private final BigDecimal subtotal;
        private final BigDecimal discountAmount;
        private final BigDecimal shippingCost;
        private final BigDecimal total;
        private final List<LineItemPreview> items;

        public OrderPreview(BigDecimal subtotal, BigDecimal discountAmount,
                BigDecimal shippingCost, BigDecimal total, List<LineItemPreview> items) {
            this.subtotal = subtotal;
            this.discountAmount = discountAmount;
            this.shippingCost = shippingCost;
            this.total = total;
            this.items = items;
        }

        public BigDecimal getSubtotal() { return subtotal; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public BigDecimal getShippingCost() { return shippingCost; }
        public BigDecimal getTotal() { return total; }
        public List<LineItemPreview> getItems() { return items; }
    }

    public static class LineItemPreview {
        private final Long productId;
        private final Long vendorId;
        private final int quantity;
        private final BigDecimal unitPrice;
        private final BigDecimal lineTotal;

        public LineItemPreview(Long productId, Long vendorId, int quantity,
                BigDecimal unitPrice, BigDecimal lineTotal) {
            this.productId = productId;
            this.vendorId = vendorId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.lineTotal = lineTotal;
        }

        public Long getProductId() { return productId; }
        public Long getVendorId() { return vendorId; }
        public int getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public BigDecimal getLineTotal() { return lineTotal; }
    }
}
