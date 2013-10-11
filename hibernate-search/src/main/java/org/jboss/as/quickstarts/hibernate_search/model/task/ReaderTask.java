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
package org.jboss.as.quickstarts.hibernate_search.model.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;

/**
 * Reads Feeds and Index data
 * @author Tharindu Jayasuriya
 *
 */
public class ReaderTask implements Runnable {
	private static final Logger log4jLogger = Logger.getLogger(ReaderTask.class);
	private FeedService feedService = null;

	public ReaderTask() {
		feedService = new FeedService();
	}
 
	/**
	 * 
	 */
	@Override
	public void run() {
		log4jLogger.info("ReaderTask.run" + new Date());
		feedService.reedAllFeeds();
		feedService.getFeedHandler().doIndex();
	}
}
