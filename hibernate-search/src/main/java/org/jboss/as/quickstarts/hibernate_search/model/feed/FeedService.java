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

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;

@Consumes({ "application/json" })
@Produces({ "application/json" })
public class FeedService {
	private static final Logger log4jLogger = Logger.getLogger(FeedService.class);
	private FeedProcessor feedProcessor = null;
	private FeedHandler feedHandler = null;

	public FeedService() {
		feedHandler = new FeedHandler();
		feedProcessor = new FeedProcessor();
	}

	/**
	 * Submit the feed
	 * 
	 * @param feedUrl
	 */
	public void submitFeed(String feedUrl) {
		if (feedHandler.checkFeed(feedUrl)) {
			return;
		}
		Feed feed = feedProcessor.processFeed(feedUrl);
		feedHandler.addFeed(feed);
		List<FeedEntry> feedEntryList = feed.getFeedEntryList();
		for (Iterator i = feedEntryList.iterator(); i.hasNext();) {
			FeedEntry feedEntry = (FeedEntry) i.next();
			feedEntry.setFeedId(feed.getId());
			feedHandler.addFeedEntry(feedEntry);
		}
	}

	/**
	 * Edit the feed
	 * 
	 * @param feedId
	 * @param feedUrl
	 */
	public void changeFeed(Integer feedId, String feedUrl) {
		Feed feed = feedProcessor.processFeed(feedUrl);
		feed.setId(feedId);
		feedHandler.editFeed(feed);
	}

	/**
	 * Delete the feed
	 * 
	 * @param feedId
	 * @param feedUrl
	 */
	public void removeFeed(Integer feedId, String feedUrl) {
		Feed feed = feedHandler.getFeed(feedId);
		feedHandler.deleteFeed(feed);
	}

	/**
	 * This will read all the listed feeds and update feed entries
	 */
	public void reedAllFeeds() {
		List<Feed> feedList = feedHandler.listAllFeeds();
		for (Iterator i = feedList.iterator(); i.hasNext();) {
			Feed feed = (Feed) i.next();
			Feed updatedFeed = feedProcessor.processFeed(feed.getUrl());
			// List<FeedEntry> feedEntryList =
			// feedHandler.getFeedEntryList(feed.getId());
			List<FeedEntry> feedEntryList = updatedFeed.getFeedEntryList();
			for (Iterator j = feedEntryList.iterator(); j.hasNext();) {
				FeedEntry feedEntry = (FeedEntry) j.next();
				if (!feedHandler.checkFeedEntry(feedEntry.getTitle())) {
					feedEntry.setFeedId(feed.getId());
					feedHandler.addFeedEntry(feedEntry);
				} else {
					// log4jLogger.info("duplicate found not updating");
				}
			}
		}
	}

	public FeedProcessor getFeedProcessor() {
		return feedProcessor;
	}

	public void setFeedProcessor(FeedProcessor feedProcessor) {
		this.feedProcessor = feedProcessor;
	}

	public FeedHandler getFeedHandler() {
		return feedHandler;
	}

	public void setFeedHandler(FeedHandler feedHandler) {
		this.feedHandler = feedHandler;
	}

	public Collection<FeedEntry> searchFeeds(String text) {
		return feedHandler.searchFeeds(text);
	}

	public void doIndex(String text) {
		feedHandler.doIndex();
	}
}
