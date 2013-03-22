/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.payment.events;

import java.util.HashMap;
import java.util.Map;

public enum PaymentTypeEnum {

    CREDIT("1"), DEBIT("2");

    private final String value;

    static Map<String, PaymentTypeEnum> map = new HashMap<String, PaymentTypeEnum>();

    static {
        for (PaymentTypeEnum paymentType : PaymentTypeEnum.values()) {
            map.put(paymentType.getValue(), paymentType);
        }
    }

    private PaymentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentTypeEnum fromString(String value) {
        return map.get(value);
    }
}
