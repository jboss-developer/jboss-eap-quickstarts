package org.jboss.as.quickstarts.hibernate_search.model.feed;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.util.HibernateUtil;


/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeedHandler {

    /**
     * Add Feed to the database
     * @param feed
     */
    public Integer addFeed(Feed feed){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer feedId = null;
        try {
            transaction = session.beginTransaction();
            feedId = (Integer) session.save(feed);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            feed.setId(feedId);
        }
        return feedId;
    }

    /**
     * Edit Feed to the database
     * @param feedUrl
     */
    public Long editFeed(String feedUrl){
        return null;
    }

    /**
     * Delete Feed to the database
     * @param feedUrl
     */
    public Long deleteFeed(String feedUrl){
        return null;
    }

    /**
     * Add FeedEntry to the database
     * @param feedEntry
     */
    public Integer addFeedEntry(FeedEntry feedEntry){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer feedId = null;
        try {
            transaction = session.beginTransaction();
            feedId = (Integer) session.save(feedEntry);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            feedEntry.setFeedEntryId(feedId);
        }
        return feedId;
    }
    
}
