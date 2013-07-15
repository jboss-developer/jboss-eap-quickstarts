package org.jboss.as.quickstarts.hibernate_search.services;

import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;

import javax.ws.rs.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/25/13
 * Time: 12:30 AM
 */
@Path("/feedReader")
@Consumes({"application/json"})
@Produces({"application/json"})
public class FeedReaderService {

    private FeedService feedService = null;

    public FeedReaderService() {
        feedService = new FeedService();
    }

    @GET
    @Path("/feeds")
    public Collection<Feed> getFeeds() {
        System.out.println("FeedReaderService.getFeeds");
        //http://localhost:8080/jboss-as-hibernate-search/services/feedReader/feeds
        return feedService.getFeedHandler().listAllFeeds();
    }

    @GET
    @Path("/feeds/{id}")
    public Feed getFeed(@PathParam("id") Integer id) {
        System.out.println("FeedReaderService.getFeed"+id);
        //http://localhost:8080/jboss-as-hibernate-search/services/feedReader/feeds/46
        return feedService.getFeedHandler().getFeed(id);
    }

    @POST
    @Path("/feeds")
    public Feed addFeed(Feed feed) {
        System.out.println("FeedReaderService.addFeed"+feed.getUrl());
        feed = feedService.getFeedProcessor().processFeed(feed.getUrl());
        feedService.getFeedHandler().addFeed(feed);
        return feed;
    }

    @PUT
    @Path("/feeds")
    public Feed updateFeed(Feed feedIn) {
        System.out.println("FeedReaderService.updateFeed"+feedIn.getId());
        Feed feed = feedService.getFeedHandler().getFeed(feedIn.getId());
        feed.setUrl(feedIn.getUrl());
        feedService.getFeedHandler().editFeed(feed);
        return feed;
    }

    @DELETE
    @Path("/feeds")
    public Feed removeFeed(Feed feedIn) {
        System.out.println("FeedReaderService.removeFeed"+feedIn.getId());
        Feed feed = feedService.getFeedHandler().getFeed(feedIn.getId());
        feedService.getFeedHandler().deleteFeed(feed);
        return feed;
    }

    @GET
    @Path("/feeds/search/{text}")
    public Collection<FeedEntry> searchFeeds(@PathParam("text") String text) {
        System.out.println("FeedReaderService.searchFeeds"+text);
        Collection<FeedEntry> feedEntries = feedService.searchFeeds(text);
        return feedEntries;
    }

    @GET
    @Path("/feedEntries")
    public Collection<FeedEntry> getFeedEntries() {
        System.out.println("FeedReaderService.getFeedEntries");
        Collection<FeedEntry> feedEntries = feedService.getFeedHandler().getFeedEntries();
        return feedEntries;
    }

    @GET
    @Path("/feedEntries/{id}")
    public Collection<FeedEntry> getFeedEntries(@PathParam("id") Integer id) {
        System.out.println("FeedReaderService.getFeedEntries+id"+id);
        //http://localhost:8080/jboss-as-hibernate-search/services/feedReader/feeds/46
        List<FeedEntry> entries = feedService.getFeedHandler().getFeedEntryList(id);
        System.out.println("FeedReaderService.getFeedEntries+entries"+entries.size());
        return entries;
    }
}
