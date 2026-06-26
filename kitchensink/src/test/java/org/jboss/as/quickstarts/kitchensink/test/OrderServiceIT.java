package org.jboss.as.quickstarts.kitchensink.test;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;
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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class OrderServiceIT {

    // Use member 2 (Robert Torres, SILVER) to avoid conflicting with other tests
    private static final Long TEST_MEMBER_ID = 2L;
    private static final String TEST_ZIP     = "27601";

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
    private OrderService orderService;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @Before
    public void cleanCartBefore() throws Exception {
        utx.begin();
        em.createQuery("DELETE FROM OrderDraftItem odi WHERE odi.memberId = :mid")
          .setParameter("mid", TEST_MEMBER_ID)
          .executeUpdate();
        utx.commit();
    }

    @After
    public void cleanCartAfter() throws Exception {
        utx.begin();
        em.createQuery("DELETE FROM OrderDraftItem odi WHERE odi.memberId = :mid")
          .setParameter("mid", TEST_MEMBER_ID)
          .executeUpdate();
        utx.commit();
    }

    /**
     * Test 1: addToCart() should persist a new order_draft_items row.
     */
    @Test
    public void testAddToCartInsertsRow() throws Exception {
        orderService.addToCart(TEST_MEMBER_ID, 1L, 5);

        utx.begin();
        Long count = em.createQuery(
                "SELECT COUNT(odi) FROM OrderDraftItem odi WHERE odi.memberId = :mid",
                Long.class)
            .setParameter("mid", TEST_MEMBER_ID)
            .getSingleResult();
        utx.commit();

        Assert.assertTrue("Cart should have at least 1 item after addToCart()", count >= 1);
    }

    /**
     * Test 2: previewOrder() on a non-empty cart should return a non-zero total.
     */
    @Test
    public void testPreviewOrderReturnsNonZeroTotal() throws Exception {
        orderService.addToCart(TEST_MEMBER_ID, 1L, 5);
        orderService.addToCart(TEST_MEMBER_ID, 3L, 10);

        OrderService.OrderPreview preview = orderService.previewOrder(TEST_MEMBER_ID, TEST_ZIP, false);

        Assert.assertNotNull("Preview should not be null", preview);
        Assert.assertTrue("Subtotal should be > 0",
            preview.getSubtotal().compareTo(BigDecimal.ZERO) > 0);
        Assert.assertTrue("Total should be > 0",
            preview.getTotal().compareTo(BigDecimal.ZERO) > 0);
        Assert.assertFalse("Items list should not be empty", preview.getItems().isEmpty());
    }

    /**
     * Test 3: submitOrder() should create a CONFIRMED order record.
     */
    @Test
    public void testSubmitOrderCreatesConfirmedOrder() throws Exception {
        orderService.addToCart(TEST_MEMBER_ID, 2L, 3);

        Long orderId = orderService.submitOrder(TEST_MEMBER_ID, TEST_ZIP, false);
        Assert.assertNotNull("Order ID should not be null after submitOrder()", orderId);

        utx.begin();
        Order order = em.find(Order.class, orderId);
        utx.commit();

        Assert.assertNotNull("Order entity should exist in DB", order);
        Assert.assertEquals("Order status should be CONFIRMED", "CONFIRMED", order.getStatus());
        Assert.assertEquals("Order member ID should match", TEST_MEMBER_ID, order.getMemberId());
    }

    /**
     * Test 4: submitOrder() should clear the member's draft cart.
     */
    @Test
    public void testSubmitOrderClearsDraftCart() throws Exception {
        orderService.addToCart(TEST_MEMBER_ID, 1L, 2);
        orderService.addToCart(TEST_MEMBER_ID, 4L, 1);

        orderService.submitOrder(TEST_MEMBER_ID, TEST_ZIP, false);

        utx.begin();
        Long remaining = em.createQuery(
                "SELECT COUNT(odi) FROM OrderDraftItem odi WHERE odi.memberId = :mid",
                Long.class)
            .setParameter("mid", TEST_MEMBER_ID)
            .getSingleResult();
        utx.commit();

        Assert.assertEquals("Draft cart should be empty after submitOrder()", 0L, remaining.longValue());
    }

    /**
     * Test 5: submitOrder() should increase the member's total_spend.
     */
    @Test
    public void testMemberTotalSpendIncreasesAfterOrder() throws Exception {
        utx.begin();
        Member memberBefore = em.find(Member.class, TEST_MEMBER_ID);
        BigDecimal spendBefore = memberBefore.getTotalSpend();
        utx.commit();

        orderService.addToCart(TEST_MEMBER_ID, 5L, 2);
        orderService.submitOrder(TEST_MEMBER_ID, TEST_ZIP, false);

        utx.begin();
        em.clear(); // force re-read from DB
        Member memberAfter = em.find(Member.class, TEST_MEMBER_ID);
        BigDecimal spendAfter = memberAfter.getTotalSpend();
        utx.commit();

        Assert.assertTrue(
            "total_spend should increase after submitOrder()",
            spendAfter.compareTo(spendBefore) > 0
        );
    }
}
