package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.jboss.as.quickstarts.kitchensink.data.ProductRepository;
import org.jboss.as.quickstarts.kitchensink.model.Product;

@ApplicationScoped
public class ShippingService {

    @Inject
    private EntityManager em;

    @Inject
    private ProductRepository productRepository;

    /**
     * Calculates the shipping cost for an order.
     *
     * @param destinationZip   the destination ZIP code
     * @param totalWeightLbs   the total weight of all items
     * @param expedite         whether expedited shipping is requested
     * @return                 the shipping cost
     */
    public BigDecimal calculateShipping(String destinationZip, BigDecimal totalWeightLbs, boolean expedite) {
        // Stored procedure: calculate_shipping(destination_zip, total_weight_lbs, expedite)
        // Logic: GREATEST(5.99, base_rate_per_lb * weight), 2.5x surcharge for expedite
        Object result = em.createNativeQuery(
                "SELECT calculate_shipping(:destinationZip, :totalWeightLbs, :expedite)")
            .setParameter("destinationZip", destinationZip)
            .setParameter("totalWeightLbs", totalWeightLbs)
            .setParameter("expedite", expedite)
            .getSingleResult();

        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        } else if (result instanceof Number) {
            return new BigDecimal(result.toString());
        } else {
            throw new IllegalStateException(
                "Unexpected result type from calculate_shipping: " + result.getClass());
        }
    }

    /**
     * Estimates shipping cost given a map of product IDs to quantities.
     * Fetches each product's weight, sums the total, then calls calculateShipping.
     *
     * @param productQuantities  map of product ID to quantity
     * @param destinationZip     the destination ZIP code
     * @param expedite           whether expedited shipping is requested
     * @return                   the estimated shipping cost
     */
    public BigDecimal estimateShipping(Map<Long, Integer> productQuantities, String destinationZip, boolean expedite) {
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey());
            if (product != null && product.getWeightLbs() != null) {
                BigDecimal itemWeight = product.getWeightLbs()
                    .multiply(BigDecimal.valueOf(entry.getValue()));
                totalWeight = totalWeight.add(itemWeight);
            }
        }
        return calculateShipping(destinationZip, totalWeight, expedite);
    }
}
