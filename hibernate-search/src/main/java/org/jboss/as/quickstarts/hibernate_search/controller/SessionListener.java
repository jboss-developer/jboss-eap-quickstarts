package org.jboss.as.quickstarts.hibernate_search.controller;

import org.jboss.as.quickstarts.hibernate_search.model.task.ReaderTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/24/13
 * Time: 10:59 PM
 */
@javax.servlet.annotation.WebListener
public class SessionListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context destroyed!");
        scheduler.shutdownNow();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context created!");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new ReaderTask(), 0, 2, TimeUnit.MINUTES);
    }


}
