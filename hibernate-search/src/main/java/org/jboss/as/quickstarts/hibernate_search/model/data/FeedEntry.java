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
package org.jboss.as.quickstarts.hibernate_search.model.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@XmlRootElement
@Indexed
@Table(name = "FeedEntry", uniqueConstraints = @UniqueConstraint(columnNames = "feedEntryId"))
public class FeedEntry implements Serializable {
	@Id
	@TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "FEED_ENTRY_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private int feedEntryId;
	private int feedId;
	private String title;
	private String author;
	// private Date publishedDate;
	private String uri;
	private String description;

	@ManyToOne
	@JoinColumn(name = "feedId", updatable = false, insertable = false)
	private Feed feed;

	public FeedEntry() {

	}

	public FeedEntry(int feedId, String title, String author,
			Date publishedDate, String uri, String description) {
		this.feedId = feedId;
		this.title = title;
		this.author = author;
		// this.publishedDate = publishedDate;
		this.uri = uri;
		this.description = description;
	}

	public int getFeedEntryId() {
		return feedEntryId;
	}

	public void setFeedEntryId(int feedEntryId) {
		this.feedEntryId = feedEntryId;
	}

	public int getFeedId() {
		return feedId;
	}

	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(name = "description", nullable = false, length = 2000)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	// public Date getPublishedDate() {
	// return publishedDate;
	// }
	//
	// public void setPublishedDate(Date publishedDate) {
	// this.publishedDate = publishedDate;
	// }

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Column(name = "description", nullable = false, length = 2000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "FeedEntry{" + "feedEntryId=" + feedEntryId + ", feedId="
				+ feedId + ", title='" + title + '\'' + ", author='" + author
				+ '\''
				+
				// ", publishedDate=" + publishedDate +
				", uri='" + uri + '\'' + ", description='" + description + '\''
				+ '}';
	}
}
