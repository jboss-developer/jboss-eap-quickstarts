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
package org.jboss.as.quickstarts.hibernate_search.services;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;
/**
 * Rest services are handled here
 * @author Tharindu Jayasuriya
 *
 */
@Path("/feedReader")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class FeedReaderService {
	private static final Logger log4jLogger = Logger.getLogger(FeedReaderService.class);

	private FeedService feedService = null;

	public FeedReaderService() {
		feedService = new FeedService();
	}
    /**
     * Get all the feeds
     * @return
     */
	@GET
	@Path("/feeds")
	public Collection<Feed> getFeeds() {
		log4jLogger.info("FeedReaderService.getFeeds");
		return feedService.getFeedHandler().listAllFeeds();
	}
    
	/**
	 * Get specific feed
	 * @param id
	 * @return
	 */
	@GET
	@Path("/feeds/{id}")
	public Feed getFeed(@PathParam("id") Integer id) {
		log4jLogger.info("FeedReaderService.getFeed" + id);
		return feedService.getFeedHandler().getFeed(id);
	}
    
	/**
	 * Add new feed
	 * @param feed
	 * @return
	 */
	@POST
	@Path("/feeds")
	public Feed addFeed(Feed feed) {
		log4jLogger.info("FeedReaderService.addFeed" + feed.getUrl());
		feed = feedService.getFeedProcessor().processFeed(feed.getUrl());
		feedService.getFeedHandler().addFeed(feed);
		return feed;
	}
    
	/**
	 * Update the feed
	 * @param feedIn
	 * @return
	 */
	@PUT
	@Path("/feeds")
	public Feed updateFeed(Feed feedIn) {
		log4jLogger.info("FeedReaderService.updateFeed" + feedIn.getId());
		Feed feed = feedService.getFeedHandler().getFeed(feedIn.getId());
		feed.setUrl(feedIn.getUrl());
		feedService.getFeedHandler().editFeed(feed);
		return feed;
	}
    
	/**
	 * Delete the feed
	 * @param feedIn
	 * @return
	 */
	@DELETE
	@Path("/feeds")
	public Feed removeFeed(Feed feedIn) {
		log4jLogger.info("FeedReaderService.removeFeed" + feedIn.getId());
		Feed feed = feedService.getFeedHandler().getFeed(feedIn.getId());
		feedService.getFeedHandler().deleteFeed(feed);
		return feed;
	}
   
	/**
	 * Search the feed data
	 * @param text
	 * @return
	 */
	@GET
	@Path("/feeds/search/{text}")
	public Collection<FeedEntry> searchFeeds(@PathParam("text") String text) {
		log4jLogger.info("FeedReaderService.searchFeeds" + text);
		Collection<FeedEntry> feedEntries = feedService.searchFeeds(text);
		return feedEntries;
	}
   
	/**
	 * Get all feed entries
	 * @return
	 */
	@GET
	@Path("/feedEntries")
	public Collection<FeedEntry> getFeedEntries() {
		log4jLogger.info("FeedReaderService.getFeedEntries");
		Collection<FeedEntry> feedEntries = feedService.getFeedHandler()
				.getFeedEntries();
		return feedEntries;
	}
    
	/**
	 * Get specific feed entry
	 * @param id
	 * @return
	 */
	@GET
	@Path("/feedEntries/{id}")
	public Collection<FeedEntry> getFeedEntries(@PathParam("id") Integer id) {
		log4jLogger.info("FeedReaderService.getFeedEntries+id" + id);
		List<FeedEntry> entries = feedService.getFeedHandler()
				.getFeedEntryList(id);
		log4jLogger.info("FeedReaderService.getFeedEntries+entries"
				+ entries.size());
		return entries;
	}
}
