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
package org.springframework.samples.travel;

import org.easymock.EasyMock;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

public class BookingFlowExecutionTests extends AbstractXmlFlowExecutionTests {

    private BookingService bookingService;

    protected void setUp() {
        bookingService = EasyMock.createMock(BookingService.class);
    }

    @Override
    protected FlowDefinitionResource getResource(FlowDefinitionResourceFactory resourceFactory) {
        return resourceFactory.createFileResource("src/main/webapp/WEB-INF/hotels/booking/booking-flow.xml");
    }

    @Override
    protected void configureFlowBuilderContext(MockFlowBuilderContext builderContext) {
        builderContext.registerBean("bookingService", bookingService);
    }

    public void testStartBookingFlow() {
        Booking booking = createTestBooking();

        EasyMock.expect(bookingService.createBooking(1L, "keith")).andReturn(booking);

        EasyMock.replay(bookingService);

        MutableAttributeMap input = new LocalAttributeMap();
        input.put("hotelId", "1");
        MockExternalContext context = new MockExternalContext();
        context.setCurrentUser("keith");
        startFlow(input, context);

        assertCurrentStateEquals("enterBookingDetails");
        assertResponseWrittenEquals("enterBookingDetails", context);
        assertTrue(getRequiredFlowAttribute("booking") instanceof Booking);

        EasyMock.verify(bookingService);
    }

    public void testEnterBookingDetails_Proceed() {
        setCurrentState("enterBookingDetails");
        getFlowScope().put("booking", createTestBooking());

        MockExternalContext context = new MockExternalContext();
        context.setEventId("proceed");
        resumeFlow(context);

        assertCurrentStateEquals("reviewBooking");
        assertResponseWrittenEquals("reviewBooking", context);
    }

    public void testReviewBooking_Confirm() {
        setCurrentState("reviewBooking");
        getFlowScope().put("booking", createTestBooking());
        MockExternalContext context = new MockExternalContext();
        context.setEventId("confirm");
        resumeFlow(context);
        assertFlowExecutionEnded();
        assertFlowExecutionOutcomeEquals("bookingConfirmed");
    }

    private Booking createTestBooking() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Jameson Inn");
        User user = new User("keith", "pass", "Keith Donald");
        Booking booking = new Booking(hotel, user);
        return booking;
    }

}
