package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * DiscountService - KEY cross-dependency bean.
 *
 * Uses BOTH:
 *   1. apply_customer_discount() stored procedure (BRONZE=2%, SILVER=5%, GOLD=8%, PLATINUM=12%)
 *   2. PricingService (shared dependency) — also used by VendorSelectionService and OrderService
 *
 * The stored procedure inserts an audit row into discount_audit on every call.
 */
@ApplicationScoped
public class DiscountService {

    @Inject
    private EntityManager em;

    @Inject
    private PricingService pricingService;

    /**
     * Calculates the discount AMOUNT for a member on a given base total.
     * The stored procedure also inserts a row into discount_audit.
     *
     * @param memberId   the member ID
     * @param baseTotal  the base order total before discount
     * @return           the discount amount (not percentage)
     */
    public BigDecimal calculateDiscount(Long memberId, BigDecimal baseTotal) {
        // Stored procedure: apply_customer_discount(member_id, base_total)
        // Returns discount AMOUNT (not percentage); inserts audit row into discount_audit
        Object result = em.createNativeQuery(
                "SELECT apply_customer_discount(:memberId, :baseTotal)")
            .setParameter("memberId", memberId)
            .setParameter("baseTotal", baseTotal)
            .getSingleResult();

        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Number) {
            return new BigDecimal(result.toString());
        } else {
            throw new IllegalStateException(
                "Unexpected result type from apply_customer_discount: " + result.getClass());
        }
    }

    /**
     * Calculates the discounted line total for a member purchasing a product.
     * Prices the line via PricingService then applies the member discount.
     *
     * @param memberId   the member ID
     * @param productId  the product ID
     * @param vendorId   the vendor ID
     * @param quantity   the order quantity
     * @return           the discounted line total
     */
    public BigDecimal getDiscountedLineTotal(Long memberId, Long productId, Long vendorId, int quantity) {
        // NOTE: PricingService is a shared dependency — also injected by OrderService and VendorSelectionService.
        // Blitzy must resolve this shared dependency when extracting stored procedure logic to @Service classes.
        BigDecimal lineTotal = pricingService.calculateLineTotal(productId, vendorId, quantity);
        BigDecimal discount = calculateDiscount(memberId, lineTotal);
        return lineTotal.subtract(discount);
    }

    /**
     * Returns the loyalty tier string for a member.
     *
     * @param memberId  the member ID
     * @return          the tier string (BRONZE, SILVER, GOLD, PLATINUM)
     */
    public String getMemberTier(Long memberId) {
        return em.createQuery(
                "SELECT m.tier FROM Member m WHERE m.id = :memberId", String.class)
            .setParameter("memberId", memberId)
            .getSingleResult();
    }
}
