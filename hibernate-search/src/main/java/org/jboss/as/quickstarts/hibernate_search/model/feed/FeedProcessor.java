package org.jboss.as.quickstarts.hibernate_search.model.feed;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jboss.as.quickstarts.hibernate_search.model.data.Feed;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 11:44 PM
 *
 */
public class FeedProcessor {

    /**
     * Parse the feed and populate feed and feed entries
     * @param sUrl
     * @return
     */
    public Feed processFeed(String sUrl){
        Feed feed = null;
        XmlReader reader = null;
        try {
            URL url = new URL(sUrl);
            reader = new XmlReader(url);
            SyndFeed syncFeed = new SyndFeedInput().build(reader);

            feed = new Feed(syncFeed.getAuthor(),syncFeed.getCopyright(),syncFeed.getDescription(),syncFeed.getTitle(),syncFeed.getLink(),url.toString());
            List<FeedEntry> feedEntryList = new ArrayList<FeedEntry>();
            for (Iterator i = syncFeed.getEntries().iterator(); i.hasNext(); ) {
                SyndEntry entry = (SyndEntry) i.next();
                FeedEntry feedEntry = new FeedEntry(feed.getId(),entry.getTitle(),entry.getAuthor(),entry.getPublishedDate(),entry.getUri(),entry.getDescription().getValue());
                feedEntryList.add(feedEntry);
            }
            feed.setFeedEntryList(feedEntryList);
        } catch (FeedException e) {
            e.printStackTrace();  
        } catch (MalformedURLException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();  
                }
        }
        return feed;
    }


}
