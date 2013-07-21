package org.jboss.as.quickstarts.hibernate_search.temp;


import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedHandler;


public class Reader {

    public static void main(String[] args) throws Exception {
        /**
         * http://www.reuters.com/tools/rss
         */
        URL url = new URL("http://feeds.reuters.com/Reuters/worldNews");
        url = new URL("http://feeds.reuters.com/reuters/sportsNews");
        
        url = new URL("http://newsrss.bbc.co.uk/rss/sportonline_uk_edition/latest_published_stories/rss.xml");
        url = new URL("http://feeds.bbci.co.uk/news/rss.xml");
        /**
         * http://www.espncricinfo.com/ci/content/rss/feeds_rss_cricket.html
         */
        url = new URL("http://www.espncricinfo.com/rss/content/story/feeds/0.xml");


//        url = new URL("http://feeds.bbci.co.uk/news/rss.xml");
        XmlReader reader = null;
        try {
            reader = new XmlReader(url);
            SyndFeed syncFeed = new SyndFeedInput().build(reader);

            FeedHandler feedHandler = new FeedHandler();
            Feed feed = new Feed(syncFeed.getAuthor(),syncFeed.getCopyright(),syncFeed.getDescription(),syncFeed.getTitle(),syncFeed.getLink(),url.toString());
            feedHandler.addFeed(feed);
            System.out.println("Feed: " + feed);
            /*System.out.println("Feed Auth: " + syncFeed.getAuthor());
            System.out.println("Feed Copy: " + syncFeed.getCopyright());
            System.out.println("Feed Desc: " + syncFeed.getDescription());
            System.out.println("Feed Link: " + syncFeed.getLink());
            System.out.println("Feed Title: " + syncFeed.getTitle());
            System.out.println("Feed Auths: " + syncFeed.getAuthors());
            System.out.println("Feed Cats: " + syncFeed.getCategories());
            System.out.println("Feed URI: " + syncFeed.getUri());
            System.out.println("Feed URI: " + syncFeed.getTitleEx());*/

            for (Iterator i = syncFeed.getEntries().iterator(); i.hasNext(); ) {
                SyndEntry entry = (SyndEntry) i.next();
                System.out.println("___________________________________________________");
                FeedEntry feedEntry = new FeedEntry(feed.getId(),entry.getTitle(),entry.getAuthor(),entry.getPublishedDate(),entry.getUri(),entry.getDescription().getValue());
                System.out.println("feedEntry"+feedEntry);
                feedHandler.addFeedEntry(feedEntry);

                System.out.println("Title:"+entry.getTitle());
                System.out.println("Author:"+entry.getAuthor());
                System.out.println("Cpntents:"+entry.getContents());
                ////System.out.println("Des:"+entry.getDescription());
                System.out.println("Pub Date:"+entry.getPublishedDate());
                System.out.println("URI:"+entry.getUri());
                System.out.println("CAts:"+entry.getCategories());

                System.out.println("Auths:"+entry.getAuthors());
                System.out.println("Contibs:"+entry.getContributors());
//                System.out.println(""+entry.getModule());
                System.out.println("Source:"+entry.getSource());
                System.out.println("UpdatedDate:"+entry.getUpdatedDate());
                System.out.println("links:"+entry.getLinks());
                System.out.println("entry:"+entry.getWireEntry());
                System.out.println("entry:"+entry.hashCode());
            }
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
