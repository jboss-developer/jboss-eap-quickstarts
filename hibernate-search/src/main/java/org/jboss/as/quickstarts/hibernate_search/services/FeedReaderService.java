package org.jboss.as.quickstarts.hibernate_search.services;

import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;

import javax.ws.rs.*;
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
public class FeedReaderService {

    private FeedService feedService = null;

    public FeedReaderService() {
        feedService = new FeedService();
    }

    @GET
    @Path("/feeds")
    public Collection<Feed> getFeeds() {
        //http://localhost:8080/jboss-as-hibernate-search/services/feedReader/feeds
        return feedService.getFeedHandler().listAllFeeds();
    }

    @GET
    @Path("/feeds/{id}")
    public Feed getFeed(@PathParam("id") Integer id) {
        //http://localhost:8080/jboss-as-hibernate-search/services/feedReader/feeds/46
        return feedService.getFeedHandler().getFeed(id);
    }

    @POST
    @Path("/feeds/{id}")
    public Feed addFeed(Feed feed) {
        feed = feedService.getFeedProcessor().processFeed(feed.getUrl());
        feedService.getFeedHandler().addFeed(feed);
        return feed;
    }

    @DELETE
    @Path("/feeds/{id}")
    public Feed removeFeed(@QueryParam("id") Integer id) {
        Feed feed = feedService.getFeedHandler().getFeed(id);
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
