/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2020, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.wildfly.quickstarts.microprofile.reactive.messaging.MessagingBean;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
@ServerSetup({RunKafkaSetupTask.class, EnableReactiveExtensionsSetupTask.class})
public class ReactiveMessagingKafkaIT {

    @ArquillianResource
    URL url;

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    private static final long TIMEOUT = 30000;

    @Deployment
    public static WebArchive getDeployment() {
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "reactive-messaging-kafka-tx.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addPackage(MessagingBean.class.getPackage())
                .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml")
                .addAsWebInfResource("META-INF/microprofile-config.properties", "classes/META-INF/microprofile-config.properties");

        return webArchive;
    }

    @Test
    public void test() throws Throwable {
        HttpGet httpGet = new HttpGet(url.toExternalForm());
        long end = System.currentTimeMillis() + TIMEOUT;
        boolean done = false;
        while (!done) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                done = checkResponse(httpResponse, System.currentTimeMillis() > end);
                Thread.sleep(1000);
            }
        }
    }

    @Test
    public void testUserApi() throws Throwable {
        Client legacyClient = ClientBuilder.newClient();
        SseEventSource sourceA = null;
        SseEventSource sourceB = null;
        try {
            final ListSubscriber taskA = new ListSubscriber(new CountDownLatch(3));
            final ListSubscriber taskB = new ListSubscriber(new CountDownLatch(3));

            sourceA = initSseSource(legacyClient,  url.toExternalForm() + "/user", taskA);
            sourceB = initSseSource(legacyClient, url.toExternalForm() + "/user", taskB);

            sendData("one");
            sendData("two");
            sendData("three");

            taskA.latch.await(60, TimeUnit.SECONDS);
            taskB.latch.await(20, TimeUnit.SECONDS);

            long end = System.currentTimeMillis() + TIMEOUT;
            while (System.currentTimeMillis() < end) {
                Thread.sleep(200);
                if (taskA.lines.size() == 3) {
                    break;
                }
            }

            checkAsynchTask(taskA, "one", "two", "three");
            checkAsynchTask(taskB, "one", "two", "three");
        } finally {
            close(legacyClient);
            close(sourceA);
            close(sourceB);
        }
    }

    private void close(SseEventSource source) {
        try {
            source.close();
        } catch (Exception ignore) {
        }
    }

    private void close(Client legacyClient) {
        try {
            legacyClient.close();
        } catch (Exception ignore) {
        }
    }

    private SseEventSource initSseSource(Client client, String url, ListSubscriber subscriber) {
        WebTarget target = client.target(url);
        SseEventSource source = SseEventSource.target(target).build();
        source.register(evt -> {
                    subscriber.onNext(evt.readData(String.class));
                },
                t -> {
                    t.printStackTrace();
                    subscriber.onError(t);
                },
                () -> {
                    // bah, never called
                    subscriber.onComplete();
                });
        source.open();
        return source;
    }

    private void sendData(String data) throws IOException {
        HttpPost post = new HttpPost(url.toExternalForm() + "/user/" + data);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        }
    }


    private void checkAsynchTask(ListSubscriber task, String... values) throws Exception {
        if (task.error != null) {
            throw (Exception) task.error;
        }
        Assert.assertEquals(3, task.lines.size());
        for (int i = 0; i < values.length; i++) {
            Assert.assertTrue("Line " + i + ": " + task.lines.get(i), task.lines.get(i).contains(values[i]));
        }
    }

    private boolean checkResponse(CloseableHttpResponse response, boolean fail) throws Throwable {
        String s;
        List<String> lines = new ArrayList<>();
        try {
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            }
            Assert.assertTrue("Expected >= 3 lines in:\n" + lines, lines.size() >= 3);
        } catch (Throwable throwable) {
            if (fail) {
                throw throwable;
            }
            return false;
        }

        Assert.assertNotEquals("Expected to find 'Hello' on line 0 of:\n" + lines, -1, lines.get(0).indexOf("Hello"));
        Assert.assertNotEquals("Expected to find 'Kafka' on line 1 of:\n" + lines, -1, lines.get(1).indexOf("Kafka"));
        for (int i = 2; i < lines.size(); i++) {
            Assert.assertNotEquals(
                    "Expected to find 'Hello' or 'Kafka' on line " + i +
                            " of:\n" + lines, -2, lines.get(i).indexOf("Hello") + lines.get(i).indexOf("Kafka"));
        }
        return true;
    }

    private static class ListSubscriber implements Subscriber<String> {
        private final CountDownLatch latch;
        final List<String> lines;
        Throwable error;

        private ListSubscriber(final CountDownLatch latch) {
            this.latch = latch;
            lines = new ArrayList<>();
        }

        @Override
        public void onSubscribe(final Subscription s) {
            s.request(latch.getCount());
        }

        @Override
        public void onNext(final String s) {
            lines.add(s);
            latch.countDown();
        }

        @Override
        public void onError(final Throwable t) {
            error = t;
            while (latch.getCount() > 0) {
                latch.countDown();
            }
        }

        @Override
        public void onComplete() {
            while (latch.getCount() > 0) {
                latch.countDown();
            }
        }
    }
}
