package org.jboss.as.quickstarts.hibernate_search.controller;

import org.jboss.as.quickstarts.hibernate_search.model.feed.FeedService;
import org.jboss.as.quickstarts.hibernate_search.model.task.ReaderTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: Tharindu Jayasuriya
 * Date: 6/24/13
 * Time: 10:59 PM
 */
@javax.servlet.annotation.WebListener
public class SessionListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;
    private static String doSchedule = System.getProperty("DO_SCHEDULE");

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context destroyed! and doSchedule"+doSchedule);
        //if(doSchedule!=null && doSchedule.equals("true")){
        	scheduler.shutdownNow();
        //}
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context created! and doSchedule"+doSchedule);
        //if(doSchedule!=null && doSchedule.equals("true")){
        	FeedService feedService = new FeedService();
            feedService.getFeedHandler().doIndex();
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(new ReaderTask(), 0, 2, TimeUnit.MINUTES);
        //}
        
        
    }


}
