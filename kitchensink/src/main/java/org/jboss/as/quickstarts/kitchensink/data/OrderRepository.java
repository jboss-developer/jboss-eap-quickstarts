package org.jboss.as.quickstarts.kitchensink.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.model.Order;
import org.jboss.as.quickstarts.kitchensink.model.OrderItem;

@ApplicationScoped
public class OrderRepository {

    @Inject
    private EntityManager em;

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findByMemberId(Long memberId) {
        return em.createQuery(
                "SELECT o FROM Order o WHERE o.memberId = :memberId ORDER BY o.createdAt DESC",
                Order.class)
            .setParameter("memberId", memberId)
            .getResultList();
    }

    public List<OrderItem> findItemsByOrderId(Long orderId) {
        return em.createQuery(
                "SELECT oi FROM OrderItem oi WHERE oi.orderId = :orderId", OrderItem.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }
}
