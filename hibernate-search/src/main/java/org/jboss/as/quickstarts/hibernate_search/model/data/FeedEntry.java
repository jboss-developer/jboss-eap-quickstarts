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
    @TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "FEED_ENTRY_SEQ" ,  allocationSize=1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private int feedEntryId;
    private int feedId;
    private String title;
    private String author;
    private Date publishedDate;
    private String uri;
    private String description;

    @ManyToOne
    @JoinColumn(name="feedId", updatable=false, insertable=false)
    private Feed feed;

    public FeedEntry(){

    }

    public FeedEntry(int feedId, String title, String author, Date publishedDate, String uri, String description) {
        this.feedId = feedId;
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
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

    @Column(name = "description", nullable = false, length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FeedEntry{" +
                "feedEntryId=" + feedEntryId +
                ", feedId=" + feedId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedDate=" + publishedDate +
                ", uri='" + uri + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
