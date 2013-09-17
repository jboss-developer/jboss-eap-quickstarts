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
package org.jboss.as.quickstarts.hibernate_search.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;
import org.jboss.as.quickstarts.hibernate_search.model.task.ReaderTask;

@javax.servlet.annotation.WebListener
public class SessionListener implements ServletContextListener {
	private static final Logger log4jLogger = Logger
			.getLogger(SessionListener.class);
	private ScheduledExecutorService scheduler;
	private static String doSchedule = System.getProperty("DO_SCHEDULE");

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log4jLogger.info("Context destroyed! and doSchedule:" + doSchedule);
		if (doSchedule != null && doSchedule.equals("true")) {
			scheduler.shutdownNow();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log4jLogger.info("Context created! and doSchedule:" + doSchedule);
		if (doSchedule != null && doSchedule.equals("true")) {
			FeedService feedService = new FeedService();
			feedService.getFeedHandler().doIndex();
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(new ReaderTask(), 0, 2,
					TimeUnit.MINUTES);
		}

	}

}
