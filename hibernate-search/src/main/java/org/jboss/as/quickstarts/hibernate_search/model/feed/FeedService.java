package org.jboss.as.quickstarts.hibernate_search.model.feed;

import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/23/13
 * Time: 9:07 PM
 */
public class FeedService {
    private FeedProcessor feedProcessor = null;
    private FeedHandler feedHandler = null;

    public FeedService() {
        feedHandler = new FeedHandler();
        feedProcessor = new  FeedProcessor();
    }

    /**
     * Submit the feed
     * @param feedUrl
     */
    public void submitFeed(String feedUrl){
        if(feedHandler.checkFeed(feedUrl)){
            return;
        }
        Feed feed = feedProcessor.processFeed(feedUrl);
        feedHandler.addFeed(feed);
        List<FeedEntry> feedEntryList = feed.getFeedEntryList();
        for (Iterator i = feedEntryList.iterator(); i.hasNext(); ) {
            FeedEntry feedEntry = (FeedEntry)i.next();
            feedEntry.setFeedId(feed.getId());
            feedHandler.addFeedEntry(feedEntry);
        }
    }

    /**
     * Edit the feed
     * @param feedId
     * @param feedUrl
     */
    public void changeFeed(Integer feedId,String feedUrl){
        Feed feed = feedProcessor.processFeed(feedUrl);
        feed.setId(feedId);
        feedHandler.editFeed(feed);
    }

    /**
     * Delete the feed
     * @param feedId
     * @param feedUrl
     */
    public void removeFeed(Integer feedId,String feedUrl){
        Feed feed = feedHandler.getFeed(feedId);
        feedHandler.deleteFeed(feed);
    }

    /**
     * This will read all the listed feeds and update feed entries
     */
    public void reedAllFeeds(){
        List<Feed> feedList = feedHandler.listAllFeeds();
        for (Iterator i = feedList.iterator(); i.hasNext(); ) {
            Feed feed = (Feed)i.next();
            Feed updatedFeed = feedProcessor.processFeed(feed.getUrl());
            //List<FeedEntry> feedEntryList = feedHandler.getFeedEntryList(feed.getId());
            List<FeedEntry> feedEntryList = updatedFeed.getFeedEntryList();
            for (Iterator j = feedEntryList.iterator(); j.hasNext(); ) {
                FeedEntry feedEntry = (FeedEntry)j.next();
                if(!feedHandler.checkFeedEntry(feedEntry.getTitle())){
                    feedEntry.setFeedId(feed.getId());
                    feedHandler.addFeedEntry(feedEntry);
                }else{
                    System.out.println("duplicate found not updating");
                }
            }
        }
    }

    public FeedProcessor getFeedProcessor() {
        return feedProcessor;
    }

    public void setFeedProcessor(FeedProcessor feedProcessor) {
        this.feedProcessor = feedProcessor;
    }

    public FeedHandler getFeedHandler() {
        return feedHandler;
    }

    public void setFeedHandler(FeedHandler feedHandler) {
        this.feedHandler = feedHandler;
    }

    public static void main(String[] args) {
        List<String> feedList = new ArrayList<String>();
//        feedList.add("http://feeds.reuters.com/Reuters/worldNews");
//        feedList.add("http://feeds.reuters.com/reuters/sportsNews");
        feedList.add("http://newsrss.bbc.co.uk/rss/sportonline_uk_edition/latest_published_stories/rss.xml");
        feedList.add("http://feeds.bbci.co.uk/news/rss.xml");
        feedList.add("http://www.espncricinfo.com/rss/content/story/feeds/0.xml");
        FeedService feedService = new FeedService();
        for (Iterator i = feedList.iterator(); i.hasNext(); ) {
            feedService.submitFeed((String)i.next());
        }
        feedService.reedAllFeeds();
    }


    public Collection<FeedEntry> searchFeeds(String text) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
