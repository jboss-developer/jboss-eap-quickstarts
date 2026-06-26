package org.jboss.as.quickstarts.kitchensink.test;

import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.kitchensink.data.OrderRepository;
import org.jboss.as.quickstarts.kitchensink.data.ProductRepository;
import org.jboss.as.quickstarts.kitchensink.data.VendorRepository;
import org.jboss.as.quickstarts.kitchensink.model.DiscountAudit;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.model.Order;
import org.jboss.as.quickstarts.kitchensink.model.OrderDraftItem;
import org.jboss.as.quickstarts.kitchensink.model.OrderItem;
import org.jboss.as.quickstarts.kitchensink.model.Product;
import org.jboss.as.quickstarts.kitchensink.model.ShippingZone;
import org.jboss.as.quickstarts.kitchensink.model.Vendor;
import org.jboss.as.quickstarts.kitchensink.model.VendorInventory;
import org.jboss.as.quickstarts.kitchensink.model.VendorInventoryId;
import org.jboss.as.quickstarts.kitchensink.service.DiscountService;
import org.jboss.as.quickstarts.kitchensink.service.OrderService;
import org.jboss.as.quickstarts.kitchensink.service.PricingService;
import org.jboss.as.quickstarts.kitchensink.service.ShippingService;
import org.jboss.as.quickstarts.kitchensink.service.TierRecalculationService;
import org.jboss.as.quickstarts.kitchensink.service.VendorSelectionService;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PricingServiceIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(
                // Models
                Member.class, Product.class, Vendor.class,
                VendorInventory.class, VendorInventoryId.class,
                Order.class, OrderItem.class, ShippingZone.class,
                DiscountAudit.class, OrderDraftItem.class,
                // Repositories
                ProductRepository.class, VendorRepository.class, OrderRepository.class,
                // Services
                PricingService.class, VendorSelectionService.class,
                DiscountService.class, ShippingService.class,
                OrderService.class, TierRecalculationService.class
            )
            .addAsResource("META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private PricingService pricingService;

    /**
     * Test 1: Basic price calculation for product 1, vendor 1, qty 1.
     * Result must be positive and greater than the product's base price ($8.49).
     * (Vendor markup pushes price above base.)
     */
    @Test
    public void testCalculatePriceBasic() {
        // Seed: product 1 = Latex Exam Gloves, base_price = $8.49, vendor 1 markup = 8%
        // Expected unit price > $8.49
        BigDecimal price = pricingService.calculatePrice(1L, 1L, 1);
        Assert.assertNotNull("Price should not be null", price);
        Assert.assertTrue("Price should be positive", price.compareTo(BigDecimal.ZERO) > 0);
        Assert.assertTrue(
            "Price with markup should exceed base price of $8.49",
            price.compareTo(new BigDecimal("8.49")) > 0
        );
    }

    /**
     * Test 2: Volume discount should reduce price at qty=100 vs qty=1.
     */
    @Test
    public void testVolumeDiscountApplied() {
        BigDecimal priceQty1   = pricingService.calculatePrice(1L, 1L, 1);
        BigDecimal priceQty100 = pricingService.calculatePrice(1L, 1L, 100);

        Assert.assertNotNull("Price qty=1 should not be null",   priceQty1);
        Assert.assertNotNull("Price qty=100 should not be null", priceQty100);
        Assert.assertTrue(
            "Unit price at qty=100 should be less than unit price at qty=1 (15% volume discount)",
            priceQty100.compareTo(priceQty1) < 0
        );
    }

    /**
     * Test 3: All volume discount tiers applied progressively.
     * qty=10 (2%), qty=20 (5%), qty=50 (10%), qty=100 (15%)
     */
    @Test
    public void testVolumeDiscountTiers() {
        BigDecimal price1   = pricingService.calculatePrice(1L, 1L, 1);
        BigDecimal price10  = pricingService.calculatePrice(1L, 1L, 10);
        BigDecimal price20  = pricingService.calculatePrice(1L, 1L, 20);
        BigDecimal price50  = pricingService.calculatePrice(1L, 1L, 50);
        BigDecimal price100 = pricingService.calculatePrice(1L, 1L, 100);

        Assert.assertTrue("qty=10 (2% disc) < no-discount",    price10.compareTo(price1)   < 0);
        Assert.assertTrue("qty=20 (5% disc) < qty=10 (2%)",    price20.compareTo(price10)  < 0);
        Assert.assertTrue("qty=50 (10% disc) < qty=20 (5%)",   price50.compareTo(price20)  < 0);
        Assert.assertTrue("qty=100 (15% disc) < qty=50 (10%)", price100.compareTo(price50) < 0);
    }

    /**
     * Test 4: Invalid product/vendor combination should throw EJBException
     * wrapping the RAISE EXCEPTION from the stored procedure.
     */
    @Test(expected = EJBException.class)
    public void testInvalidVendorProductComboThrowsException() {
        pricingService.calculatePrice(99L, 99L, 1);
    }
}
