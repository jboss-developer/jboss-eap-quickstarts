package org.jboss.as.quickstarts.hibernate_search.controller;

import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/25/13
 * Time: 12:30 AM
 */
@Path("/feedReader")
@Consumes({"application/json"})
@Produces({"application/json"})
public class FeedReader extends Application {

    private FeedService feedService = null;

    public FeedReader() {
        feedService = new FeedService();
    }

    @GET
    @Path("/feeds")
    public Collection<Feed> getFeeds() {
        return feedService.getFeedHandler().listAllFeeds();
    }

    @GET
    @Path("/feeds/{feedId}")
    public Feed getFeed(@PathParam("feedId") Integer feedId) {
        return feedService.getFeedHandler().getFeed(feedId);
    }

    @PUT
    @Path("/feeds/{addFeed}")
    public Feed addFeed(@QueryParam("feedUrl") String url) {
        Feed feed = feedService.getFeedProcessor().processFeed(url);
        feedService.getFeedHandler().addFeed(feed);
        return feed;
    }

    @POST
    @Path("/feeds/{updateFeed}")
    public Feed updateFeed(@QueryParam("feedId") Integer feedId, @QueryParam("feedUrl") String url) {
        Feed feed = feedService.getFeedProcessor().processFeed(url);
        feed.setId(feedId);
        feedService.getFeedHandler().editFeed(feed);
        return feed;
    }

    @DELETE
    @Path("/feeds/{removeFeed}")
    public Feed removeFeed(@QueryParam("feedId") Integer feedId, @QueryParam("feedUrl") String url) {
        Feed feed = feedService.getFeedProcessor().processFeed(url);
        feed.setId(feedId);
        feedService.getFeedHandler().deleteFeed(feed);
        return feed;
    }

    @POST
    @Path("/feeds/{searchFeeds}")
    public Collection<FeedEntry> searchFeeds(@QueryParam("text") String text) {
        Collection<FeedEntry> feedEntries = feedService.searchFeeds(text);
        return feedEntries;
    }
}
