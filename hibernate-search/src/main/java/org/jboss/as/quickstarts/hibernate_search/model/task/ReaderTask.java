package org.jboss.as.quickstarts.hibernate_search.model.task;

import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/24/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReaderTask implements Runnable{

    private FeedService feedService = null;

    public ReaderTask() {
        feedService = new FeedService();
    }

    @Override
    public void run() {
        System.out.println("ReaderTask.run"+new Date());
        feedService.reedAllFeeds();
    }
}
