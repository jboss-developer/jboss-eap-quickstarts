/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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

APPMODULE.namespace('APPMODULE.app.getContacts');
APPMODULE.namespace('APPMODULE.app.buildContactList');
APPMODULE.namespace('APPMODULE.app.getContactById');
APPMODULE.namespace('APPMODULE.app.buildContactDetail');
APPMODULE.namespace('APPMODULE.app.restEndpoint');

APPMODULE.app.restEndpoint = 'rest/members';

/**
 * It is recommended to bind to this event instead of DOM ready() because this will work regardless of whether 
 * the page is loaded directly or if the content is pulled into another page as part of the Ajax navigation system.
 * 
 * The first thing you learn in jQuery is to call code inside the $(document).ready() function so everything 
 * will execute as soon as the DOM is loaded. However, in jQuery Mobile, Ajax is used to load the contents of 
 * each page into the DOM as you navigate, and the DOM ready handler only executes for the first page. 
 * To execute code whenever a new page is loaded and created, you can bind to the pageinit event. 
 * 
 * 
 * These functions perform the GET. They display the list, detailed list, and fill in the update form.
 * 
 * @author Joshua Wilson
 */
$( document ).on( "pageinit", function(mainEvent) {
    //Initialize the vars in the beginning so that you will always have access to them.
    var getCurrentTime = APPMODULE.util.getCurrentTime,
        restEndpoint = APPMODULE.app.restEndpoint;
    
    console.log(getCurrentTime() + " [js/app.js] (document -> pageinit) - start");
    
    /* 
     * Make sure the Contacts list gets populated but only once.
     * 
     * The "pagebeforeshow" event will delay this function until everything is set up.
     * 
     * Because of the interesting jQM loading architecture, multiple event triggering is a constant problem. 
     * The "e.handled" if statement used here and elsewhere is meant to keep jQM from running this code multiple 
     * times for one display. 
     */
    $('#contacts-list-page').on( "pagebeforeshow", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-list-page -> pagebeforeshow) - start");
            
            // Fetches the initial Contact data.
            APPMODULE.app.getContacts();
            
            e.handled = true;
            console.log(getCurrentTime() + " [js/app.js] (#contacts-list-page -> pagebeforeshow) - end");
        }
    });
    
    // This is called on 'pagebeforeshow' above and by the APPMODULE.submissions
    // Uses JAX-RS GET to retrieve current member list. 
    APPMODULE.app.getContacts = function () {
        console.log(getCurrentTime() + " [js/app.js] (getContacts) - start");
        var jqxhr = $.ajax({
            url: restEndpoint,
            cache: false,
            type: "GET"
        }).done(function(data, textStatus, jqXHR) {
            console.log(getCurrentTime() + " [js/app.js] (getContacts) - succes on ajax call");
            APPMODULE.app.buildContactList(data);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log(getCurrentTime() + " [js/app.js] (getContacts) - error in ajax - " +
                        " - jqXHR = " + jqXHR.status +
                        " - textStatus = " + textStatus +
                        " - errorThrown = " + errorThrown);
        });
        console.log(getCurrentTime() + " [js/app.js] (getContacts) - end");
    };

    // This is called by APPMODULE.app.getContacts.
    // Display contact list on page one.
    APPMODULE.app.buildContactList = function (contacts) {
        console.log(getCurrentTime() + " [js/app.js] (buildContactList) - start");
        var contactList = "",
            contactDetailList = "";
        
        // The data from the AJAX call is not sorted alphabetically, this will fix that.
        contacts.sort(function(a,b){
              var aName = a.firstName.toLowerCase() + a.lastName.toLowerCase();
              var bName = b.firstName.toLowerCase() + b.lastName.toLowerCase(); 
              return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
        });
        
        // Pull the info out of the Data returned from the AJAX request and create the HTML to be placed on the page.
        $.each(contacts, function(index, contact) {
            // Create the HTML for the List only view.
            contactList = contactList.concat(
                "<li id=list-contact-ID-" + contact.id.toString() + " class=contacts-list-item >" +
                    "<a href='#contacts-edit-page' >" + contact.firstName.toString() + " " + contact.lastName.toString() + "</a>" +
                "</li>");
            // Create the HTML for the Detailed List view.
            contactDetailList = contactDetailList.concat(
                "<li id=detail-contact-ID-" + contact.id.toString() + " class=contacts-detail-list-item >" +
                    "<a href='#contacts-edit-page' >" + contact.firstName.toString() + " " + contact.lastName.toString() + "</a>" +
                    "<div class='detialedList'>" +
                        "<p><strong>" + contact.email.toString() + "</strong></p>" +
                        "<p>" + contact.phoneNumber.toString() + "</p>" +
                        "<p>" + APPMODULE.util.convertUTCToDate(contact.birthDate) + "</p>" +
                    "</div>" +
                 "</li>");
        });
        
        // Start with a clean list element otherwise we would have repeats.
        $('#contacts-display-listview').empty();
        
        // Check if it is already initialized or not, refresh the list in case it is initialized otherwise trigger create.
        if ( $('#contacts-display-listview').hasClass('ui-listview')) {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - hasClass ui-listview) - append.listview - start");
            $('#contacts-display-listview').append(contactList).listview("refresh", true);
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - hasClass ui-listview) - append.listview - end");
        } 
        else {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - !hasClass ui-listview) - append.trigger - start");
            $('#contacts-display-listview').append(contactList).trigger('create');
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - !hasClass ui-listview) - append.trigger - end");
        }        
        
        // Start with a clean list element otherwise we would have repeats.
        $('#contacts-display-detail-listview').empty();
        
        // check if it is already initialized or not, refresh the list in case it is initialized otherwise trigger create
        if ( $('#contacts-display-detail-listview').hasClass('ui-listview')) {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - hasClass ui-listview) - append.listview - start");
            $('#contacts-display-detail-listview').append(contactDetailList).listview("refresh", true);
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - hasClass ui-listview) - append.listview - end");
        } 
        else {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - !hasClass ui-listview) - append.trigger - start");
            $('#contacts-display-detail-listview').append(contactDetailList).trigger('create');
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - !hasClass ui-listview) - append.trigger - end");
        }        
        
        // Attach onclick event to each row of the contact list that will open up the contact info to be edited.
        $('.contacts-list-item').on("click", function(event){
            if(event.handled !== true) {
                console.log(getCurrentTime() + " [js/app.js] (.contacts-display-listview -> on click) - start");
                
                APPMODULE.app.getContactById($(this).attr("id").split("list-contact-ID-").pop());
                
                event.handled = true;
                console.log(getCurrentTime() + " [js/app.js] (.contacts-display-listview -> on click) - end");
            }
        });
        
        // Attach onclick event to each row of the contact list detailed page that will open up the contact info to be edited.
        $('li.contacts-detail-list-item').on("click", function(event){
            if(event.handled !== true) {
                console.log(getCurrentTime() + " [js/app.js] (li.contacts-display-listview -> on click) - start");
                
                APPMODULE.app.getContactById($(this).attr("id").split("detail-contact-ID-").pop());
                
                // Turn the whole <li> into a link.
                $.mobile.changePage("#contacts-edit-page");
                
                event.handled = true;
                console.log(getCurrentTime() + " [js/app.js] (li.contacts-display-listview -> on click) - end");
            }
        });
        
        console.log(getCurrentTime() + " [js/app.js] (buildContactList) - end");
        // Add in a line to visually see when we are done.
        console.log("-----------------------------List Page---------------------------------------");
    };
    
    // This is called by the on click event list above.
    // Retrieve employee detail based on employee id.
    APPMODULE.app.getContactById = function (contactID) {
        console.log(getCurrentTime() + " [js/app.js] (getContactById) - start");
        console.log(getCurrentTime() + " [js/app.js] (getContactById) - contactID = " + contactID);
    
        var jqxhr = $.ajax({
            url: restEndpoint + "/" + contactID.toString(),
            cache: false,
            type: "GET"
        }).done(function(data, textStatus, jqXHR) {
            console.log(getCurrentTime() + " [js/app.js] (getContactById) - success on ajax call");
            APPMODULE.app.buildContactDetail(data);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log(getCurrentTime() + " [js/app.js] (getContactById) - error in ajax" +
                        " - jqXHR = " + jqXHR.status +
                        " - textStatus = " + textStatus +
                        " - errorThrown = " + errorThrown);
        });
        console.log(getCurrentTime() + " [js/app.js] (getContactById) - end");
    };
    
    // This is called by APPMODULE.app.getContactById.
    // Display contact detail for editing on the Edit page.
    APPMODULE.app.buildContactDetail = function(contact) {
        console.log(getCurrentTime() + " [js/app.js] (buildContactDetail) - start");
        
        // Put each field value in the text input on the page.
        $('#contacts-edit-input-firstName').val(contact.firstName);
        $('#contacts-edit-input-lastName').val(contact.lastName);
        $('#contacts-edit-input-tel').val(contact.phoneNumber);
        $('#contacts-edit-input-email').val(contact.email);
        $('#contacts-edit-input-date').val(APPMODULE.util.convertUTCToDate(contact.birthDate));
        $('#contacts-edit-input-id').val(contact.id);
        
        console.log(getCurrentTime() + " [js/app.js] (buildContactDetail) - end");
        // Add in a line to visually see when we are done.
        console.log("-----------------------------Update Page---------------------------------------");
    };
    
    console.log(getCurrentTime() + " [js/app.js] (document -> pageinit) - end");
});


