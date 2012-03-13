package org.jboss.as.quickstarts.jms;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;

public class JMSClientUtil {
    private static final Logger log = Logger.getLogger(JMSClientUtil.class.getName());

    private static final String DEFAULT_CONN_TYPE = "netty";
    private static final String DEFAULT_CF_JNDI = "RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION_JNDI = "testQueue";
    private static final String DEFAULT_USERNAME = "defaultUsername";
    private static final String DEFAULT_PASSWORD = "defaultPassword";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";

    public static ConnectionFactory getConnectionFactory() throws Exception {
        InitialContext context = null;
        ConnectionFactory connectionFactory = null;

        try {
            String connType = System.getProperty("connection.type", DEFAULT_CONN_TYPE);
            String cfName = System.getProperty("cf.name", DEFAULT_CF_JNDI);

            log.info("Attempting to acquire ConnectionFactory \"" + cfName + "\" with a connection type of: " + connType);

            if (connType.equalsIgnoreCase("jndi")) {
                final Properties env = new Properties();
                env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
                env.put(Context.PROVIDER_URL, PROVIDER_URL);
                env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
                env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
                context = new InitialContext(env);
                connectionFactory = (ConnectionFactory) context.lookup(cfName);
                log.info("Found ConnectionFactory " + cfName + " in JNDI");
            } else {
                log.info("Creating Netty Based ConnectionFactory.");
                TransportConfiguration config = new TransportConfiguration(NettyConnectorFactory.class.getName());
                connectionFactory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, config);
            }

            return connectionFactory;

        } finally {
            closeResources(context);
        }

    }

    public static Destination getDestination() throws Exception {
        InitialContext context = null;
        Destination destination = null;

        try {
            String connType = System.getProperty("connection.type", DEFAULT_CONN_TYPE);
            String destName = System.getProperty("dest.name", DEFAULT_DESTINATION_JNDI);

            log.info("Attempting to acquire Destination with a connection type of: " + connType);

            if (connType.equalsIgnoreCase("jndi")) {
                final Properties env = new Properties();
                env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
                env.put(Context.PROVIDER_URL, PROVIDER_URL);
                env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
                env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
                context = new InitialContext(env);
                destination = (Destination) context.lookup(destName);
                log.info("Found Destination " + destName + " in JNDI");
            } else {
                log.info("HornetQ Destination " + destName);
                destination = HornetQJMSClient.createQueue(destName);
            }
            return destination;
        } finally {
            closeResources(context);
        }
    }

    public static void closeResources(Object... objects) {
        try {
            for (Object object : objects) {
                Method close = object.getClass().getMethod("close", new Class[] {});
                close.invoke(object, new Object[] {});
            }
        } catch (Exception ignore) {
        }

    }
}
