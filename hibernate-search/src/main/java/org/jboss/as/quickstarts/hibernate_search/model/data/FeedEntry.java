package org.jboss.as.quickstarts.hibernate_search.model.data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * User: Tharindu Jayasuriya
 */
@Entity
@XmlRootElement
@Table(name = "FeedEntry", uniqueConstraints = @UniqueConstraint(columnNames = "feedEntryId"))
public class FeedEntry implements Serializable {
    @Id
    private Long feedEntryId;
    private Long feedId;
    private String title;
    private String author;
    private Date publishedDate;
    private String uri;
    private String description;

    @ManyToOne
    private Feed feed;

    @TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "FEED_ENTRY_SEQ"
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    public Long getFeedEntryId() {
        return feedEntryId;
    }

    public void setFeedEntryId(Long feedEntryId) {
        this.feedEntryId = feedEntryId;
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
