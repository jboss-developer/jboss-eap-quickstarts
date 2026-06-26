package org.jboss.as.quickstarts.kitchensink.service;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.logging.Logger;

/**
 * TierRecalculationService - nightly job calling recalculate_customer_tiers() stored procedure.
 *
 * Tier thresholds based on 90-day rolling spend:
 *   BRONZE   < $500
 *   SILVER   $500  - $1,999.99
 *   GOLD     $2,000 - $4,999.99
 *   PLATINUM >= $5,000
 */
@Singleton
@Startup
public class TierRecalculationService {

    @Inject
    private EntityManager em;

    @Inject
    private Logger log;

    @Schedule(hour = "2", minute = "0", second = "0", persistent = false)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void runNightlyTierRecalculation() {
        log.info("TierRecalculationService: starting nightly tier recalculation");
        // Stored procedure: recalculate_customer_tiers()
        // Loops all members, recalculates tier from 90-day order spend, updates only if changed
        em.createNativeQuery("SELECT recalculate_customer_tiers()").getSingleResult();
        log.info("TierRecalculationService: nightly tier recalculation complete");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void triggerRecalculation() {
        log.info("TierRecalculationService: manual recalculation triggered");
        em.createNativeQuery("SELECT recalculate_customer_tiers()").getSingleResult();
        log.info("TierRecalculationService: manual recalculation complete");
    }
}
