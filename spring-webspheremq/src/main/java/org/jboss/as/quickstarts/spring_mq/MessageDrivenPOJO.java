/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.spring_mq;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * A Spring Message Driven POJO
 * 
 * @author dgrove
 * 
 */
public class MessageDrivenPOJO implements MessageListener {
    /** Logger for the class. */
    private static final Logger logger = Logger.getLogger(MessageDrivenPOJO.class);

    /** Spring messaging template */
    private JmsTemplate jmsTemplate;
    private Queue queue;

    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @Override
    public void onMessage(Message message) {
        logger.info("Entering onMessage");

        if (message instanceof TextMessage) {
            try {
                final String text = ((TextMessage) message).getText();
                // print the message
                logger.info(text);

                // re-send the message
                this.jmsTemplate.send(this.queue, new MessageCreator() {
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(text);
                    }
                });
            } catch (JMSException e) {
                logger.error(e);
            }
        }
    }

    /**
     * Injected from the Spring Context
     * 
     * @param queue
     */
    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    /**
     * Injected from the Spring Context
     * 
     * @param cf
     */
    public void setConnectionFactory(ConnectionFactory cf) {
        this.jmsTemplate = new JmsTemplate(cf);
    }
}