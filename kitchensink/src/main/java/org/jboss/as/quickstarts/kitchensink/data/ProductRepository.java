package org.jboss.as.quickstarts.kitchensink.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.model.Product;

@ApplicationScoped
public class ProductRepository {

    @Inject
    private EntityManager em;

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public Product findBySku(String sku) {
        return em.createQuery("SELECT p FROM Product p WHERE p.sku = :sku", Product.class)
            .setParameter("sku", sku)
            .getSingleResult();
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p ORDER BY p.name", Product.class)
            .getResultList();
    }

    public List<Product> findByCategory(String category) {
        return em.createQuery(
                "SELECT p FROM Product p WHERE p.category = :category ORDER BY p.name", Product.class)
            .setParameter("category", category)
            .getResultList();
    }
}
