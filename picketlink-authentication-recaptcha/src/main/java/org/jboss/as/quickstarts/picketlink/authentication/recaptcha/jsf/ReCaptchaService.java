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
package org.jboss.as.quickstarts.picketlink.authentication.recaptcha.jsf;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

/**
 * <p>Simple service that uses the ReCAPTCHA Verification API to validate challenge/responses.</p>
 *
 */
public class ReCaptchaService {

    public static final String RECAPTCHA_PRIVATE_KEY = "6LdjBeMSAAAAAB8HeTU0ihREtwJyIiKUqK6x-6pL";

    public boolean verify(String challenge, String challengeResponse) {
        ClientRequest request = new ClientRequest("http://www.google.com/recaptcha/api/verify");

        request.setHttpMethod("GET");

        request.queryParameter("privatekey", RECAPTCHA_PRIVATE_KEY);
        request.queryParameter("remoteip", "localhost");
        request.queryParameter("challenge", challenge);
        request.queryParameter("response", challengeResponse);

        ClientResponse<String> serviceResponse;

        try {
            serviceResponse = request.get();
        } catch (Exception e) {
            throw new RuntimeException("Could not verify captcha.", e);
        }

        int status = serviceResponse.getStatus();

        if (status == 200) {
            return serviceResponse.getEntity(String.class).contains("true");
        }

        return false;
    }

}
