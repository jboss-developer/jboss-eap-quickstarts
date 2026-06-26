package org.jboss.as.quickstarts.kitchensink.test;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
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
public class DiscountServiceIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(
                Member.class, Product.class, Vendor.class,
                VendorInventory.class, VendorInventoryId.class,
                Order.class, OrderItem.class, ShippingZone.class,
                DiscountAudit.class, OrderDraftItem.class,
                ProductRepository.class, VendorRepository.class, OrderRepository.class,
                PricingService.class, VendorSelectionService.class,
                DiscountService.class, ShippingService.class,
                OrderService.class, TierRecalculationService.class
            )
            .addAsResource("META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private DiscountService discountService;

    @Inject
    private PricingService pricingService;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    /**
     * Test 1: BRONZE member (member 3) discount on $100 base should be approximately 2%.
     * Seed: member 3 = Emily Chen, tier = BRONZE
     */
    @Test
    public void testBronzeMemberDiscountIsApproximatelyTwoPercent() {
        BigDecimal baseTotal = new BigDecimal("100.00");
        // apply_customer_discount(3, 100.00) should return 2.00 for BRONZE
        BigDecimal discount = discountService.calculateDiscount(3L, baseTotal);
        Assert.assertNotNull("Discount should not be null", discount);
        Assert.assertTrue("BRONZE 2% discount on $100 should be between $1.99 and $2.01",
            discount.compareTo(new BigDecimal("1.99")) >= 0 &&
            discount.compareTo(new BigDecimal("2.01")) <= 0
        );
    }

    /**
     * Test 2: GOLD member (member 1) discount on $100 base should be approximately 8%.
     * Seed: member 1 = Jane Smith, tier = GOLD
     */
    @Test
    public void testGoldMemberDiscountIsApproximatelyEightPercent() {
        BigDecimal baseTotal = new BigDecimal("100.00");
        // apply_customer_discount(1, 100.00) should return 8.00 for GOLD
        BigDecimal discount = discountService.calculateDiscount(1L, baseTotal);
        Assert.assertNotNull("Discount should not be null", discount);
        Assert.assertTrue("GOLD 8% discount on $100 should be between $7.99 and $8.01",
            discount.compareTo(new BigDecimal("7.99")) >= 0 &&
            discount.compareTo(new BigDecimal("8.01")) <= 0
        );
    }

    /**
     * Test 3: Each call to calculateDiscount() should insert a new row into discount_audit.
     */
    @Test
    public void testDiscountAuditRowCreated() throws Exception {
        utx.begin();
        Long beforeCount = em.createQuery("SELECT COUNT(da) FROM DiscountAudit da", Long.class)
            .getSingleResult();
        utx.commit();

        discountService.calculateDiscount(2L, new BigDecimal("50.00"));

        utx.begin();
        Long afterCount = em.createQuery("SELECT COUNT(da) FROM DiscountAudit da", Long.class)
            .getSingleResult();
        utx.commit();

        Assert.assertEquals("discount_audit should have exactly one more row after calculateDiscount()",
            beforeCount + 1, afterCount.longValue());
    }

    /**
     * Test 4: getDiscountedLineTotal() result must be less than calculateLineTotal()
     * because a discount was applied via the shared PricingService dependency.
     */
    @Test
    public void testGetDiscountedLineTotalUsesSharedPricingService() {
        // product 1, vendor 1, qty 5, member 2 (SILVER)
        BigDecimal lineTotal      = pricingService.calculateLineTotal(1L, 1L, 5);
        BigDecimal discountedLine = discountService.getDiscountedLineTotal(2L, 1L, 1L, 5);

        Assert.assertNotNull("Line total should not be null",           lineTotal);
        Assert.assertNotNull("Discounted line total should not be null", discountedLine);
        Assert.assertTrue(
            "Discounted line total must be less than undiscounted line total",
            discountedLine.compareTo(lineTotal) < 0
        );
    }
}
