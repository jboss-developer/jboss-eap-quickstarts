/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.quickstarts.hibernate3.data;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Simple class which shows what has been chosen by gamer. It contains history of previous hits.
 * We could annotated "Game" class, however its cleaner to keep this separate for sake of example simplicity.
 * <br>
 * This class will be converted to entries in DB with Hibernate3 and JPA. 
 * @author baranowb
 *
 */
@Entity
@Table(name = "GameHistory")
public class GameHistory {

    @Id
    @GeneratedValue
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date time;
    /**
     * The number that the user needs to guess
     */
    @Column
    private Integer number;

    /**
     * The users latest guess
     */
    @Column
    private Integer guess;

    /**
     * The smallest number guessed so far (so we can track the valid guess range).
     */
    @Column
    private Integer smallest;

    /**
     * The largest number guessed so far
     */
    @Column
    private Integer biggest;

    /**
     * The number of guesses remaining
     */
    @Column
    private Integer remainingGuesses;


    /**
     * 
     */
    public GameHistory() {
        super();
        // default CTR to allow JPA/Hibernate magic
    }

    /**
     *
     * @param number
     * @param guess
     * @param smallest
     * @param biggest
     * @param remainingGuesses
     */
    public GameHistory( Integer number, Integer guess, Integer smallest, Integer biggest, Integer remainingGuesses) {
        super();
        // custom CTR, so its easier to create instances
        this.time = new Date();
        this.number = number;
        this.guess = guess;
        this.smallest = smallest;
        this.biggest = biggest;
        this.remainingGuesses = remainingGuesses;
    }

    // ---------- property getters ----------
    
    public Long getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getGuess() {
        return guess;
    }

    public Integer getSmallest() {
        return smallest;
    }

    public Integer getBiggest() {
        return biggest;
    }

    public Integer getRemainingGuesses() {
        return remainingGuesses;
    }
    
   // ---------- small util method ----------
    
    public boolean isSuccess()
    {
        return this.getGuess().equals(this.getNumber());
    }
}
