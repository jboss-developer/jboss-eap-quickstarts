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
package org.jboss.as.quickstarts.hibernate_search.model.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.data.SequenceBean;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedProcessor;

public class HibernateUtil {
	private static final Logger log4jLogger = Logger.getLogger(HibernateUtil.class);
	private static final SessionFactory sessionFactory;

	static {
		try {

			Configuration cfg = new Configuration()
					.addAnnotatedClass(
							org.jboss.as.quickstarts.hibernate_search.model.data.Feed.class)
					.addAnnotatedClass(SequenceBean.class)
					.addAnnotatedClass(FeedEntry.class).configure();
			sessionFactory = cfg.buildSessionFactory();
			// sessionFactory = new
			// Configuration().configure().buildSessionFactory();
			// sessionFactory = new
			// AnnotationConfiguration().configure().buildSessionFactory();//Annotation
			// Configuration

		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
