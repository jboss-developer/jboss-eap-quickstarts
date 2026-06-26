package org.jboss.as.quickstarts.kitchensink.test;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
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
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TierRecalculationIT {

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
    private TierRecalculationService tierRecalculationService;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    // Track test member IDs for cleanup
    private Long testMemberId = null;
    private Long testOrderId  = null;

    @After
    public void cleanup() throws Exception {
        utx.begin();
        if (testOrderId != null) {
            em.createQuery("DELETE FROM OrderItem oi WHERE oi.orderId = :oid")
              .setParameter("oid", testOrderId).executeUpdate();
            em.createQuery("DELETE FROM Order o WHERE o.id = :oid")
              .setParameter("oid", testOrderId).executeUpdate();
        }
        if (testMemberId != null) {
            em.createQuery("DELETE FROM DiscountAudit da WHERE da.memberId = :mid")
              .setParameter("mid", testMemberId).executeUpdate();
            em.createQuery("DELETE FROM OrderDraftItem odi WHERE odi.memberId = :mid")
              .setParameter("mid", testMemberId).executeUpdate();
            em.createQuery("DELETE FROM Order o WHERE o.memberId = :mid")
              .setParameter("mid", testMemberId).executeUpdate();
            em.createQuery("DELETE FROM Member m WHERE m.id = :mid")
              .setParameter("mid", testMemberId).executeUpdate();
        }
        testMemberId = null;
        testOrderId  = null;
        utx.commit();
    }

    /**
     * Helper: creates and persists a new test member with unique email.
     */
    private Member createTestMember(String tier, BigDecimal totalSpend) throws Exception {
        utx.begin();
        Member m = new Member();
        m.setName("Test Member");
        m.setEmail("test-" + UUID.randomUUID() + "@test.com");
        m.setPhoneNumber("9195559999");
        m.setTier(tier);
        m.setTotalSpend(totalSpend);
        em.persist(m);
        utx.commit();
        testMemberId = m.getId();
        return m;
    }

    /**
     * Helper: creates and persists a CONFIRMED order for a member with a given total and timestamp.
     */
    private Order createTestOrder(Long memberId, BigDecimal total, LocalDateTime createdAt) throws Exception {
        utx.begin();
        Order o = new Order();
        o.setMemberId(memberId);
        o.setStatus("CONFIRMED");
        o.setSubtotal(total);
        o.setDiscountAmount(BigDecimal.ZERO);
        o.setShippingCost(BigDecimal.ZERO);
        o.setTotal(total);
        o.setCreatedAt(createdAt);
        em.persist(o);
        utx.commit();
        testOrderId = o.getId();
        return o;
    }

    /**
     * Test 1: A new member with no orders should remain BRONZE after recalculation.
     */
    @Test
    public void testNewMemberRemainsBronze() throws Exception {
        Member member = createTestMember("BRONZE", BigDecimal.ZERO);

        tierRecalculationService.triggerRecalculation();

        utx.begin();
        em.clear();
        Member updated = em.find(Member.class, member.getId());
        utx.commit();

        Assert.assertEquals("New member with no orders should remain BRONZE",
            "BRONZE", updated.getTier());
    }

    /**
     * Test 2: Member with $750 in CONFIRMED orders within 30 days should become SILVER.
     * SILVER threshold: $500 - $1,999.99 (90-day rolling spend).
     */
    @Test
    public void testMemberUpgradesToSilverAfterSpend() throws Exception {
        Member member = createTestMember("BRONZE", BigDecimal.ZERO);
        createTestOrder(member.getId(), new BigDecimal("750.00"), LocalDateTime.now().minusDays(15));

        tierRecalculationService.triggerRecalculation();

        utx.begin();
        em.clear();
        Member updated = em.find(Member.class, member.getId());
        utx.commit();

        Assert.assertEquals("Member with $750 spend in 30 days should be upgraded to SILVER",
            "SILVER", updated.getTier());
    }

    /**
     * Test 3: Member with $3,000 in CONFIRMED orders within 90 days should become GOLD.
     * GOLD threshold: $2,000 - $4,999.99 (90-day rolling spend).
     */
    @Test
    public void testMemberUpgradesToGoldAfterSpend() throws Exception {
        Member member = createTestMember("BRONZE", BigDecimal.ZERO);
        createTestOrder(member.getId(), new BigDecimal("3000.00"), LocalDateTime.now().minusDays(45));

        tierRecalculationService.triggerRecalculation();

        utx.begin();
        em.clear();
        Member updated = em.find(Member.class, member.getId());
        utx.commit();

        Assert.assertEquals("Member with $3,000 spend in 45 days should be upgraded to GOLD",
            "GOLD", updated.getTier());
    }

    /**
     * Test 4: A GOLD member whose orders are all older than 91 days should drop to BRONZE.
     * Old orders fall outside the 90-day rolling window and no longer count toward tier.
     */
    @Test
    public void testMemberDoesNotDowngradeOnOldOrders() throws Exception {
        Member member = createTestMember("GOLD", new BigDecimal("3000.00"));
        // Place $3,000 order but 92 days ago — outside the 90-day window
        createTestOrder(member.getId(), new BigDecimal("3000.00"), LocalDateTime.now().minusDays(92));

        tierRecalculationService.triggerRecalculation();

        utx.begin();
        em.clear();
        Member updated = em.find(Member.class, member.getId());
        utx.commit();

        // The 90-day spend is $0 because all orders are older than 90 days.
        // Per recalculate_customer_tiers() logic: $0 spend -> BRONZE
        Assert.assertEquals(
            "GOLD member with all orders > 91 days old should drop to BRONZE (no qualifying 90-day spend)",
            "BRONZE", updated.getTier()
        );
    }
}
