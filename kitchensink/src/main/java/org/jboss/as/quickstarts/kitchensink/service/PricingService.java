package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;

@ApplicationScoped
public class PricingService {

    @Inject
    private EntityManager em;

    /**
     * Calculates the unit price for a product from a specific vendor at a given quantity.
     * Delegates to the calculate_price() PostgreSQL stored function.
     *
     * @param productId  the product ID
     * @param vendorId   the vendor ID
     * @param quantity   the order quantity (affects volume discount tier)
     * @return           the unit price as BigDecimal
     */
    public BigDecimal calculatePrice(Long productId, Long vendorId, int quantity) {
        // Stored procedure: calculate_price(product_id, vendor_id, quantity)
        // Logic: base_price * (1 + markup%) * (1 - volume_discount%)
        // Volume tiers: qty>=100->15%, qty>=50->10%, qty>=20->5%, qty>=10->2%
        Object result = em.createNativeQuery(
                "SELECT calculate_price(:productId, :vendorId, :quantity)")
            .setParameter("productId", productId)
            .setParameter("vendorId", vendorId)
            .setParameter("quantity", quantity)
            .getSingleResult();

        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Number) {
            return new BigDecimal(result.toString());
        } else {
            throw new IllegalStateException(
                "Unexpected result type from calculate_price: " + result.getClass());
        }
    }

    /**
     * Calculates the total price for a line item (unit price * quantity).
     *
     * @param productId  the product ID
     * @param vendorId   the vendor ID
     * @param quantity   the order quantity
     * @return           the line total as BigDecimal
     */
    public BigDecimal calculateLineTotal(Long productId, Long vendorId, int quantity) {
        BigDecimal unitPrice = calculatePrice(productId, vendorId, quantity);
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
