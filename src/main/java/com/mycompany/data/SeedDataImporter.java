package com.mycompany.data;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.logging.Category;

import com.mycompany.model.Member;

/**
 * Import seed data into the database on application startup.
 * 
 * <p>
 * Observes the context initialized event and loads seed data into the database using JPA.
 * </p>
 * 
 * <p>
 * As an alternative, you can perform the data loading by observing the context initialized event of a ServletContextListener
 * </p>
 * 
 * @author Dan Allen
 */
@Startup
@Singleton
public class SeedDataImporter {
    @Inject
    @Category("jboss-javaee6-webapp-src")
    private Logger log;

    @Inject
    @MemberRepository
    private EntityManager em;

    @Inject
    private UserTransaction tx;

    @PostConstruct
    public void importData() {
        Member member1 = new Member();
        member1.setName("John Smith");
        member1.setEmail("john.smith@mailinator.com");
        member1.setPhoneNumber("2125551212");
        try {
            try {
                em.persist(member1);
            } catch (TransactionRequiredException e) {
                // manual transaction control required in @PostConstruct method
                // only use if enforced by JPA provider (due to bug in GlassFish)
                tx.begin();
                em.persist(member1);
                tx.commit();
            }
            log.info("Successfully imported seed data.");
        } catch (Exception e) {
            log.warn("Seed data import failed.", e);
        }
    }
}
