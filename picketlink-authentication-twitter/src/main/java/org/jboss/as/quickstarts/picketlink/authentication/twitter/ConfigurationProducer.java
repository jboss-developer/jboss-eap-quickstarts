/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.authentication.twitter;

import org.picketlink.social.auth.conf.FacebookConfiguration;
import org.picketlink.social.auth.conf.TwitterConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Class to obtain the configuration
 * @author Anil Saldhana
 * @since May 31, 2013
 */
@ApplicationScoped
public class ConfigurationProducer {
    @Produces
    public TwitterConfiguration configure(){
        final String clientID  = System.getProperty("TWIT_CLIENT_ID");
        final String clientSecret = System.getProperty("TWIT_CLIENT_SECRET");
        final String returnURL = System.getProperty("TWIT_RETURN_URL");
        final String scope = "email";

        TwitterConfiguration twitterConfiguration = new TwitterConfiguration() {
            @Override
            public String getClientID() {
                return clientID;
            }

            @Override
            public String getClientSecret() {
                return clientSecret;
            }

            @Override
            public String getScope() {
                return scope;
            }

            @Override
            public String getReturnURL() {
                return returnURL;
            }
        };
        return twitterConfiguration;
    }
}