/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.hibernate_search.model.feed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.util.HibernateUtil;

/**
 * Feed Handler used to add/edit/delete Feeds and Contents of the Feed 
 * @author Tharindu Jayasuriya
 *
 */
public class FeedHandler {
	private static final Logger log4jLogger = Logger.getLogger(FeedHandler.class);
	private static int MAX_ROWS = 50;


	public FeedHandler() {
	}

	/**
	 * Add Feed to the database
	 * 
	 * @param feed
	 */
	public Integer addFeed(Feed feed) {
		if (feed.getDescription() != null
				&& feed.getDescription().length() > 4500) {
			feed.setDescription(feed.getDescription().substring(0, 4500));
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		Integer feedId = null;
		try {
			transaction = session.beginTransaction();
			feedId = (Integer) session.save(feed);
			transaction.commit();
		} catch (HibernateException e) {
			log4jLogger.info("EX HAPPENED" + feed);
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
			feed.setId(feedId);
		}
		return feedId;
	}

	/**
	 * Method to READ all the Feeds
	 */
	public List<Feed> listAllFeeds() {
		List<Feed> feedList = new ArrayList<Feed>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List feedData = session.createQuery("from Feed").list();
			for (Iterator iterator = feedData.iterator(); iterator.hasNext();) {
				Feed feed = (Feed) iterator.next();
				feedList.add(feed);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return feedList;
	}

	/**
	 * Edit Feed in the database
	 * 
	 * @param feed
	 */
	public Integer editFeed(Feed feed) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Integer feedId = null;
		try {
			tx = session.beginTransaction();
			session.update(feed);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return feedId;
	}

	/**
	 * Delete Feed in the database
	 * 
	 * @param feed
	 */
	public Integer deleteFeed(Feed feed) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Integer feedId = null;
		try {
			tx = session.beginTransaction();
			session.delete(feed);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return feedId;
	}

	/**
	 * Add FeedEntry to the database
	 * 
	 * @param feedEntry
	 */
	public Integer addFeedEntry(FeedEntry feedEntry) {
		if (feedEntry.getDescription() != null
				&& feedEntry.getDescription().length() > 4500) {
			feedEntry.setDescription(feedEntry.getDescription().substring(0,
					4500));
		}
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
	 * 
	 * @param feedEntry
	 */
	public Integer addOrUpdateFeedEntry(FeedEntry feedEntry) {
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
	 * Check the existence of the feed in the database
	 * 
	 * @param feedEntryTitle
	 */
	public boolean checkFeedEntry(String feedEntryTitle) {
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
	 * 
	 * @param feedId
	 */
	public List<FeedEntry> getFeedEntryList(int feedId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		List<FeedEntry> feedEntryList = null;
		try {
			transaction = session.beginTransaction();
			String hql = "from FeedEntry feedEntry where feedEntry.feedId = :theFeedId order by feedentryid desc";
			Query query = session.createQuery(hql);
			query.setInteger("theFeedId", feedId);
			feedEntryList = query.list();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (feedEntryList != null && feedEntryList.size() > MAX_ROWS) {
			return feedEntryList.subList(0, MAX_ROWS);
		} else if (feedEntryList != null && feedEntryList.size() > 0) {
			return feedEntryList.subList(0, feedEntryList.size());
		} else {
			return feedEntryList;
		}
	}

	/**
	 * Check the existence of the feed url in the database
	 * 
	 * @param feedUrl
	 */
	public boolean checkFeed(String feedUrl) {
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
	 * 
	 * @param feedId
	 * @return
	 */
	public Feed getFeed(Integer feedId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Feed feed = null;
		try {
			tx = session.beginTransaction();
			feed = (Feed) session.get(Feed.class, feedId);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return feed;
	}

	/**
	 * Get all feed entries
	 * @return
	 */
	public Collection<FeedEntry> getFeedEntries() {
		List<FeedEntry> feedEntries = new ArrayList<FeedEntry>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String hql = "from FeedEntry order by feedentryid desc";
			Query query = session.createQuery(hql);
			feedEntries = query.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (feedEntries != null && feedEntries.size() > MAX_ROWS) {
			return feedEntries.subList(0, MAX_ROWS);
		} else if (feedEntries != null && feedEntries.size() > 0) {
			return feedEntries.subList(0, feedEntries.size());
		} else {
			return feedEntries;
		}
	}

	/**
	 * This will index the Feed data
	 */
	public void doIndex() {
		log4jLogger.info("######################################FeedHandler.doIndex");
		Session session = HibernateUtil.getSessionFactory().openSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		try {
			fullTextSession.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fullTextSession.close();
	}

	/**
	 * This will query the indexes created on defined attributes
	 * @param queryString
	 * @return
	 */
	public List<FeedEntry> searchFeeds(String queryString) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		QueryBuilder queryBuilder = fullTextSession.getSearchFactory()
				.buildQueryBuilder().forEntity(FeedEntry.class).get();
		org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword()
				.onFields("description", "author", "title")
				.matching(queryString).createQuery();

		// wrap Lucene query in a javax.persistence.Query
		org.hibernate.Query fullTextQuery = fullTextSession
				.createFullTextQuery(luceneQuery, FeedEntry.class);

		List<FeedEntry> contactList = fullTextQuery.list();
		fullTextSession.close();
		return contactList;
	}
}
