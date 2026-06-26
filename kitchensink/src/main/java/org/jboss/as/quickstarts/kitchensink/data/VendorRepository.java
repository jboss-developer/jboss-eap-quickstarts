package org.jboss.as.quickstarts.kitchensink.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.model.Vendor;
import org.jboss.as.quickstarts.kitchensink.model.VendorInventory;

@ApplicationScoped
public class VendorRepository {

    @Inject
    private EntityManager em;

    public Vendor findById(Long id) {
        return em.find(Vendor.class, id);
    }

    public List<Vendor> findAll() {
        return em.createQuery(
                "SELECT v FROM Vendor v ORDER BY v.fulfillmentRating DESC", Vendor.class)
            .getResultList();
    }

    public List<VendorInventory> findInventoryForProduct(Long productId) {
        return em.createQuery(
                "SELECT vi FROM VendorInventory vi WHERE vi.id.productId = :productId",
                VendorInventory.class)
            .setParameter("productId", productId)
            .getResultList();
    }
}
