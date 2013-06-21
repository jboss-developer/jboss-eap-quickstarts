package org.jboss.as.quickstarts.hibernate_search.model.data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Tharindu Jayasuriya
 */

@Entity
@XmlRootElement
@Table(name = "Feed", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Feed implements Serializable {
    @Id
    private Long id;
    private String author;
    private String copyright;
    private String description;
    private String title;
    private String link;
    private String url;

    @OneToMany(mappedBy = "Feed")
    private List<FeedEntry> feedEntryList;

    public Feed() {
    }


    @TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "FEED_SEQ"
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<FeedEntry> getFeedEntryList() {
        return feedEntryList;
    }

    public void setFeedEntryList(List<FeedEntry> feedEntryList) {
        this.feedEntryList = feedEntryList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
