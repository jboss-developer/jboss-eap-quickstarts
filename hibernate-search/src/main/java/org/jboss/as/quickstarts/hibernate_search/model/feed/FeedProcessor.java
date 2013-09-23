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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * From the feed url feed and content of the feed is extracted
 * @author Tharindu Jayasuriya
 *
 */
public class FeedProcessor {
	private static final Logger log4jLogger = Logger.getLogger(FeedProcessor.class);

	/**
	 * Parse the feed and populate feed and feed entries
	 * 
	 * @param sUrl
	 * @return
	 */
	public Feed processFeed(String sUrl) {
		Feed feed = null;
		XmlReader reader = null;
		try {
			URL url = new URL(sUrl);
			reader = new XmlReader(url);
			SyndFeed syncFeed = new SyndFeedInput().build(reader);

			feed = new Feed(syncFeed.getAuthor(), syncFeed.getCopyright(),
					syncFeed.getDescription(), syncFeed.getTitle(),
					syncFeed.getLink(), url.toString());
			List<FeedEntry> feedEntryList = new ArrayList<FeedEntry>();
			for (Iterator i = syncFeed.getEntries().iterator(); i.hasNext();) {
				SyndEntry entry = (SyndEntry) i.next();
				FeedEntry feedEntry = new FeedEntry(feed.getId(),
						entry.getTitle(), entry.getAuthor(),
						entry.getPublishedDate(), entry.getUri(), entry
								.getDescription().getValue());
				feedEntryList.add(feedEntry);
			}
			feed.setFeedEntryList(feedEntryList);
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return feed;
	}

}
