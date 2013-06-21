package org.jboss.as.quickstarts.hibernate_search.model;

import java.util.List;

/**
 * User: Tharindu Jayasuriya
 */
public class Feed {

    private String author;
    private String copyright;
    private String description;
    private String title;
    private String link;
    private String url;
    
    private List<FeedEntry> feedEntryList;

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
