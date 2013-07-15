package org.jboss.as.quickstarts.hibernate_search.model.feed;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.as.quickstarts.hibernate_search.infinispan.InfinispanFeedServiceHandler;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.util.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 9:44 PM
 *
 */
public class FeedHandler {

    private static int MAX_ROWS = 20;
    private InfinispanFeedServiceHandler infinispanFeedServiceHandler = null;

    public FeedHandler() {
        infinispanFeedServiceHandler = new InfinispanFeedServiceHandler();
    }

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
            System.out.println("EX HAPPENED"+feed);
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            feed.setId(feedId);
        }
        return feedId;
    }

    /**
     *   Method to  READ all the Feeds
     */
    public List<Feed> listAllFeeds( ){
        List<Feed> feedList = new ArrayList<Feed>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List feedData = session.createQuery("from Feed").list();
            for (Iterator iterator =
                         feedData.iterator(); iterator.hasNext();){
                Feed feed = (Feed) iterator.next();
                feedList.add(feed);
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return feedList;
    }

    /**
     * Edit Feed to the database
     * @param feed
     */
    public Integer editFeed(Feed feed){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer feedId = null;
        try{
            tx = session.beginTransaction();
            //Feed feedFromDb = (Feed)session.get(Feed.class, feed.getId());
            session.update(feed);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return feedId;
    }

    /**
     * Delete Feed to the database
     * @param feed
     */
    public Integer deleteFeed(Feed feed){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer feedId = null;
        try{
            tx = session.beginTransaction();
            //Feed feedFromDb = (Feed)session.get(Feed.class, feed.getId());
            session.delete(feed);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return feedId;
    }

    /**
     * Add FeedEntry to the database
     * @param feedEntry
     */
    public Integer addFeedEntry(FeedEntry feedEntry){
        infinispanFeedServiceHandler.getInfinispanFeedService().insertNewFeedEntry(feedEntry);
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

    /**
     * Add or Update FeedEntry to the database
     * @param feedEntry
     */
    public Integer addOrUpdateFeedEntry(FeedEntry feedEntry){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer feedId = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(feedEntry);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            feedId = feedEntry.getFeedEntryId();
        }
        return feedId;
    }

    /**
     * Check the existance of the feed in the database
     * @param feedEntryTitle
     */
    public boolean checkFeedEntry(String feedEntryTitle){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer feedEntry = null;
        try {
            transaction = session.beginTransaction();
            String hql = "select 1 from FeedEntry feedEntry where feedEntry.title = :theTitle";
            Query query = session.createQuery(hql);
            query.setString("theTitle", feedEntryTitle);
            feedEntry = (Integer) query.uniqueResult();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return feedEntry != null;
    }

    /**
     * Get the feed entries in the database for a Feed
     * @param feedId
     */
    public List<FeedEntry> getFeedEntryList(int feedId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        List<FeedEntry> feedEntryList = null;
        try {
            transaction = session.beginTransaction();
            String hql = "from FeedEntry feedEntry where feedEntry.feedId = :theFeedId";
            Query query = session.createQuery(hql);
            query.setInteger("theFeedId", feedId);
            query.setMaxResults(MAX_ROWS);
            feedEntryList = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return feedEntryList;
    }

    /**
     * Check the existance of the feed url in the database
     * @param feedUrl
     */
    public boolean checkFeed(String feedUrl){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer feed = null;
        try {
            transaction = session.beginTransaction();
            String hql = "select 1 from Feed feed where feed.url = :theUrl";
            Query query = session.createQuery(hql);
            query.setString("theUrl", feedUrl);
            feed = (Integer) query.uniqueResult();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return feed != null;
    }

    /**
     * Get the Feed from the id
     * @param feedId
     * @return
     */
    public Feed getFeed(Integer feedId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Feed feed = null;
        try{
            tx = session.beginTransaction();
            feed = (Feed)session.get(Feed.class, feedId);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return feed;
    }

    public Collection<FeedEntry> getFeedEntries() {
        List<FeedEntry> feedEntries = new ArrayList<FeedEntry>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            /*tx = session.beginTransaction();
            List feedData = session.createQuery("from FeedEntry").list();
            for (Iterator iterator =
                         feedData.iterator(); iterator.hasNext();){
                FeedEntry feed = (FeedEntry) iterator.next();
                feedEntries.add(feed);
            }
            tx.commit();*/

            tx = session.beginTransaction();
            String hql = "from FeedEntry";
            Query query = session.createQuery(hql);
            query.setMaxResults(MAX_ROWS);
            feedEntries = query.list();
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return feedEntries;
    }

    public void doIndex() {
        System.out.println("######################################FeedHandler.doIndex");
        Session session = HibernateUtil.getSessionFactory().openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        try {
            fullTextSession.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fullTextSession.close();
    }

    public List<FeedEntry> searchFeeds(String queryString) {
        infinispanFeedServiceHandler.getInfinispanFeedService().doQuery(queryString);
        Session session = HibernateUtil.getSessionFactory().openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(FeedEntry.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("description","author","title").matching(queryString).createQuery();

        // wrap Lucene query in a javax.persistence.Query
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, FeedEntry.class);

        List<FeedEntry> contactList = fullTextQuery.list();

        fullTextSession.close();

        return contactList;
    }
}
