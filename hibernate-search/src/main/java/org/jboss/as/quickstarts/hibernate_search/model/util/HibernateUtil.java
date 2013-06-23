package org.jboss.as.quickstarts.hibernate_search.model.util;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 6/21/13
 * Time: 11:50 PM
 *
 */

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;
import org.jboss.as.quickstarts.hibernate_search.model.data.SequenceBean;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {

            Configuration cfg = new Configuration()
                    .addAnnotatedClass(org.jboss.as.quickstarts.hibernate_search.model.data.Feed.class)
                    .addAnnotatedClass(SequenceBean.class)
                    .addAnnotatedClass(FeedEntry.class)
            .configure();
            sessionFactory = cfg.buildSessionFactory();
           // sessionFactory = new Configuration().configure().buildSessionFactory();
//            sessionFactory =  new AnnotationConfiguration().configure().buildSessionFactory();//Annotation Configuration

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
