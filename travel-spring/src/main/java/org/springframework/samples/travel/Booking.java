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

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * A Hotel Booking made by a User.
 */
@Entity
public class Booking implements Serializable {

    private Long id;

    private User user;

    private Hotel hotel;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date checkinDate;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date checkoutDate;

    private String creditCard;

    private String creditCardName;

    private int creditCardExpiryMonth;

    private int creditCardExpiryYear;

    private boolean smoking;

    private int beds;

    private Set<Amenity> amenities;

    public Booking() {
        Calendar calendar = Calendar.getInstance();
        setCheckinDate(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        setCheckoutDate(calendar.getTime());
    }

    public Booking(Hotel hotel, User user) {
        this();
        this.hotel = hotel;
        this.user = user;
    }

    @Transient
    public BigDecimal getTotal() {
        return hotel.getPrice().multiply(new BigDecimal(getNights()));
    }

    @Transient
    public int getNights() {
        if (checkinDate == null || checkoutDate == null) {
            return 0;
        } else {
            return (int) (checkoutDate.getTime() - checkinDate.getTime())
                    / 1000 / 60 / 60 / 24;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date datetime) {
        this.checkinDate = datetime;
    }

    @ManyToOne
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Transient
    public String getDescription() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return hotel == null ? null : hotel.getName() + ", "
                + df.format(getCheckinDate()) + " to "
                + df.format(getCheckoutDate());
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public int getCreditCardExpiryMonth() {
        return creditCardExpiryMonth;
    }

    public void setCreditCardExpiryMonth(int creditCardExpiryMonth) {
        this.creditCardExpiryMonth = creditCardExpiryMonth;
    }

    public int getCreditCardExpiryYear() {
        return creditCardExpiryYear;
    }

    public void setCreditCardExpiryYear(int creditCardExpiryYear) {
        this.creditCardExpiryYear = creditCardExpiryYear;
    }

    @Transient
    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

    // TODO replace with JSR 303
    public void validateEnterBookingDetails(ValidationContext context) {
        MessageContext messages = context.getMessageContext();
        if (checkinDate.before(today())) {
            messages.addMessage(new MessageBuilder().error().source(
                    "checkinDate").code("booking.checkinDate.beforeToday")
                    .build());
        } else if (checkoutDate.before(checkinDate)) {
            messages.addMessage(new MessageBuilder().error().source(
                    "checkoutDate").code(
                    "booking.checkoutDate.beforeCheckinDate").build());
        }
    }

    private Date today() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    @Override
    public String toString() {
        return "Booking(" + user + "," + hotel + ")";
    }

}
