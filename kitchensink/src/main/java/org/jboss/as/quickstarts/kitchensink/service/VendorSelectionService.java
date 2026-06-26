package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.model.Vendor;
import org.jboss.as.quickstarts.kitchensink.model.VendorInventory;

/**
 * VendorSelectionService - calls select_best_vendor() stored procedure.
 * Also uses PricingService (shared dependency) to build a ranked vendor list for display.
 */
@ApplicationScoped
public class VendorSelectionService {

    @Inject
    private EntityManager em;

    @Inject
    private PricingService pricingService;

    /**
     * Selects the best vendor for a given product and quantity using the stored procedure.
     *
     * @param productId  the product ID
     * @param quantity   the requested quantity
     * @return           the vendor ID of the best vendor, or null if none found
     */
    public Long selectBestVendor(Long productId, int quantity) {
        // Stored procedure: select_best_vendor(product_id, quantity)
        // Scoring: price 60%, fulfillment_rating 30%, avg_shipping_days 10%
        // Falls back to cheapest vendor with any stock if no vendor has enough quantity
        Object result = em.createNativeQuery(
                "SELECT select_best_vendor(:productId, :quantity)")
            .setParameter("productId", productId)
            .setParameter("quantity", quantity)
            .getSingleResult();

        if (result == null) {
            return null;
        } else if (result instanceof Long) {
            return (Long) result;
        } else if (result instanceof Number) {
            return ((Number) result).longValue();
        } else {
            throw new IllegalStateException(
                "Unexpected result type from select_best_vendor: " + result.getClass());
        }
    }

    /**
     * Returns a ranked list of all vendors with their prices for a given product/quantity.
     * Fetches all vendor inventory, prices each via PricingService, and sorts by unit price ascending.
     *
     * @param productId  the product ID
     * @param quantity   the requested quantity
     * @return           sorted list of VendorPriceResult (cheapest first)
     */
    public List<VendorPriceResult> getVendorPricesForProduct(Long productId, int quantity) {
        List<VendorInventory> inventoryList = em.createQuery(
                "SELECT vi FROM VendorInventory vi WHERE vi.id.productId = :productId",
                VendorInventory.class)
            .setParameter("productId", productId)
            .getResultList();

        List<VendorPriceResult> results = new ArrayList<>();
        for (VendorInventory vi : inventoryList) {
            Long vendorId = vi.getId().getVendorId();
            Vendor vendor = em.find(Vendor.class, vendorId);
            if (vendor == null) {
                continue;
            }
            BigDecimal unitPrice;
            try {
                unitPrice = pricingService.calculatePrice(productId, vendorId, quantity);
            } catch (Exception e) {
                // Skip vendors for which pricing cannot be determined
                continue;
            }
            VendorPriceResult vpr = new VendorPriceResult();
            vpr.vendorId = vendorId;
            vpr.vendorName = vendor.getName();
            vpr.unitPrice = unitPrice;
            vpr.fulfillmentRating = vendor.getFulfillmentRating();
            vpr.avgShippingDays = vendor.getAvgShippingDays();
            results.add(vpr);
        }

        results.sort(Comparator.comparing(r -> r.unitPrice));
        return results;
    }

    /**
     * Holds price and vendor metadata for a single vendor option.
     */
    public static class VendorPriceResult {
        public Long vendorId;
        public String vendorName;
        public BigDecimal unitPrice;
        public BigDecimal fulfillmentRating;
        public Integer avgShippingDays;
    }
}
