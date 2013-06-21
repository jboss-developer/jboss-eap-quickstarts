package org.jboss.as.quickstarts.hibernate_search;


import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class Reader {

    public static void main(String[] args) throws Exception {
        /**
         * http://www.reuters.com/tools/rss
         */
        URL url = new URL("http://feeds.reuters.com/Reuters/worldNews");
        url = new URL("http://feeds.reuters.com/reuters/sportsNews");
        /**
         * http://news.bbc.co.uk/sport2/hi/help/rss/
         */
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
            SyndFeed feed = new SyndFeedInput().build(reader);
            System.out.println("Fee d Auth: " + feed.getAuthor());
            System.out.println("Feed Copy: " + feed.getCopyright());
            System.out.println("Feed Desc: " + feed.getDescription());
            System.out.println("Feed Link: " + feed.getLink());
            System.out.println("Feed Title: " + feed.getTitle());
            System.out.println("Feed Auths: " + feed.getAuthors());
            System.out.println("Feed Cats: " + feed.getCategories());
            System.out.println("Feed URI: " + feed.getUri());
            System.out.println("Feed URI: " + feed.getTitleEx());

            for (Iterator i = feed.getEntries().iterator(); i.hasNext(); ) {
                SyndEntry entry = (SyndEntry) i.next();
                System.out.println("___________________________________________________");
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
