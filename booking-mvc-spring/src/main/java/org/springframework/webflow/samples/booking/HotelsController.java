/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.webflow.samples.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class HotelsController {

    private BookingService bookingService;

    @Autowired
    public HotelsController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
    public void search(SearchCriteria searchCriteria, Principal currentUser, Model model) {
        if (currentUser != null) {
            List<Booking> booking = bookingService.findBookings(currentUser.getName());
            model.addAttribute(booking);
        }
    }

    @RequestMapping(value = "/hotels", method = RequestMethod.GET)
    public String list(SearchCriteria criteria, Model model) {
        List<Hotel> hotels = bookingService.findHotels(criteria);
        model.addAttribute(hotels);
        return "hotels/list";
    }

    @RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute(bookingService.findHotelById(id));
        return "hotels/show";
    }

    @RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
    public String deleteBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:../hotels/search";
    }

}
