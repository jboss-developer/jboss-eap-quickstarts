package org.jboss.as.quickstarts.xa;

import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class XAService {
    /**
     *  Ask the container to inject persistence contexts corresponding to 2 different JPA entity
     *  managers (ie 2 separate databases).
     *  The unit names correspond to the ones defined in the war archives' persistence.xml file
     */
    @PersistenceContext(unitName = "pctx1")
    EntityManager em1;
    @PersistenceContext(unitName = "pctx2")
    EntityManager em2;

    // Inject a UserTransaction for manual transaction demarcation.
    @Inject
    private UserTransaction userTransaction;

    /**
     * Update a key value database. If the key is already in the database a runtime exception is thrown
     *
     * @param entityManager an open JPA entity manager
     * @param key if not null then a pair is inserted into the database
     * @param value the value to be associated with the key
     *
     * @return all pairs
     */
    public String updateKeyValueDatabase(EntityManager entityManager, String key, String value) {
        StringBuilder sb = new StringBuilder();

        if (key != null && key.length() != 0) {
            // insert into the key/value table (which will throw an error if the key already exists)
            entityManager.persist(new KVPair(key, value));
        }

        // list all key value pairs
        final List<KVPair> list = entityManager.createQuery("select k from KVPair k").getResultList();

        for (KVPair kvPair : list)
            sb.append(kvPair.getKey()).append("=").append(kvPair.getValue()).append(',');

        return sb.toString();
    }

    /**
     * Update a key value database. If the key is already in the database a runtime exception is thrown
     *
     * @param key if not null then a pair is inserted into the database
     * @param value the value to be associated with the key
     * @param entityManagerIds the entity managers involved in the update (ie we are updated more than
     * one database)
     * @return all pairs
     */
    public String updateKeyValueDatabase(String key, String value, String ... entityManagerIds) {
        try {
            userTransaction.begin();

            StringBuilder result = new StringBuilder();

            for (String emId : entityManagerIds) {
                if ("1".equals(emId)) {
                    // use entity manager corresponding to persistency context 1 to update the database
                    result.append("Database 1: ").append(updateKeyValueDatabase(em1, key, value)).append("<br/>");
                } else if ("2".equals(emId)) {
                    // use entity manager corresponding to persistency context 2 to update the database
                    result.append("Database 2: ").append(updateKeyValueDatabase(em2, key, value)).append("<br/>");
                } else {
                    result.append("Database ").append(emId).append(" does not exist (use em=1 or em=2 or em=1,2)<br/>");
                }
            }

            userTransaction.commit();

            return result.toString();
        } catch (RollbackException e) {
            /*
             * We tried to commit the transaction but it has already been rolled back (most probably because
             * of duplicate key).
             */
            Throwable t = e.getCause();

            return t != null ? t.getMessage() :  e.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
           try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
                    userTransaction.rollback();
            } catch (Throwable e) {
                // ignore
            }
        }
    }
}
