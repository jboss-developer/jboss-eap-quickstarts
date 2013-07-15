package org.jboss.as.quickstarts.hibernate_search.infinispan;

import org.infinispan.Cache;
import org.infinispan.lucene.InfinispanDirectory;
import org.infinispan.manager.DefaultCacheManager;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 7/15/13
 * Time: 6:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class InfinispanFeedServiceHandler {

    private InfinispanFeedService infinispanFeedService = null;

    public  InfinispanFeedServiceHandler() {
        DefaultCacheManager cacheManager = null;
        try {
            cacheManager = new DefaultCacheManager("lucene-demo-cache-config.xml");
            cacheManager.start();
            Cache<?, ?> cache = cacheManager.getCache();
            InfinispanDirectory directory = new InfinispanDirectory(cache);
            infinispanFeedService = new InfinispanFeedService(directory, cache);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cacheManager.stop();
        }
    }

    public InfinispanFeedService getInfinispanFeedService() {
        return infinispanFeedService;
    }

    public void setInfinispanFeedService(InfinispanFeedService infinispanFeedService) {
        this.infinispanFeedService = infinispanFeedService;
    }
}
