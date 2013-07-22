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

/*
 * Core JavaScript functionality for the application.  Performs the required
 * Restful calls, validates return values, and populates the member table.
 * 
 * @Author: Joshua Wilson
 */

/* Builds the updated table for the member list */
// Load the application once the DOM is ready, using `jQuery.ready`:
$(function() {
	/*
	 * Models are the heart of any JavaScript application, containing the
	 * interactive data as well as a large part of the logic surrounding it:
	 * conversions, validations, computed properties, and access control.
	 * You extend Backbone.Model with your domain-specific methods, and Model
	 * provides a basic set of functionality for managing changes.
	 */
	// Our basic **Member** model
	window.Member = Backbone.Model.extend({
		//Intentionally left empty
	});

	/*
	 * Collections are ordered sets of models. You can bind "change" events to be
	 * notified when any model in the collection has been modified, listen for "add"
	 * and "remove" events, fetch the collection from the server, and use a full
	 * suite of Underscore.js methods.
	 *
	 * Any event that is triggered on a model in a collection will also be triggered
	 * on the collection directly, for convenience. This allows you to listen for
	 * changes to specific attributes in any model in a collection, for example:
	 * Documents.on("change:selected", ...)
	 */
	window.MemberList = Backbone.Collection.extend({
		// Specify the base url to target the rest-easy service
		url : 'rest/members',

		// Reference to this collection's model.
		model : Member
	});

	// Create our global collection of **Members**.
	window.Members = new MemberList;


	/*
	 * Backbone Views are almost more convention than they are code — they don't
	 * determine anything about your HTML or CSS for you, and can be used with any
	 * JavaScript templating library. The general idea is to organize your
	 * interface into logical views, backed by models, each of which can be
	 * updated independently when the model changes, without having to redraw the
	 * page. Instead of digging into a JSON object, looking up an element in the
	 * DOM, and updating the HTML by hand, you can bind your view's render function
	 * to the model's "change" event — and now everywhere that model data is
	 * displayed in the UI, it is always immediately up to date.
	 *
	 */
	window.MemberView = Backbone.View.extend({

		// The HTML that gets created will be inserted into a parent element defined here.
		// The default is 'div' so we don't need to list it.
//		el : "div",
		className : "row member body",

		// Cache the template function for a single item.
		template : _.template($('#member-Body-tmpl').html()),

		// The MemberView listens for changes to its model, re-rendering.
		initialize : function() {
//			console.log("MemberView - initialize() - start");
			_.bindAll(this, 'render');

			// Listen to model changes
			this.model.on('change', this.render, this);
		},

		// Re-render the contents of the member item.
		render : function() {
//			console.log("MemberView - render() - start");
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		},

	});

	// The Application
	// ---------------

	// Our overall **AppView** is the top-level piece of UI.
	window.AppView = Backbone.View.extend({

		// Instead of generating a new element, bind to the existing skeleton of
		// the App already present in the HTML.
		el : $("#container"),

		// By default, delegateEvents (events) is called within the View's constructor for you, so if you have a
		//  simple events hash, all of your DOM events will always already be connected, and you will never
		//  have to call this function yourself.
		events : {
			"click #refreshButtonD" : "updateMemberTable",
			"click #refreshButtonM" : "updateMemberTable",
			"click #cancel" : "cancelRegistration",
			"pagebeforeshow #register-art" : "clearMessages"
		},

		// At initialization we bind to the relevant events on the `Members`
		// collection, when items are added or changed. Kick things off by
		// loading any preexisting members that might be saved in
		// *localStorage*.
		initialize : function() {
//			console.log("AppView - initialize() - start");
			//refer to the parent function. JS is block scoped.
			var self = this;

			Members.on('add', this.addOneMember, this);
			Members.on('reset', this.addAllMemebers, this);
			Members.on('all', this.render, this);

			$('#members').html(self.templateHeaderRow);
			this.updateMemberTable();
//			Members.fetch();
			this.submitRegistration();
		},

		// Re-rendering the App just means refreshing the statistics -- the rest
		// of the app doesn't change.
		render: function() {
//			console.log("AppView - render() - start");
		},

		// Add a single member item to the list by creating a view for it, and
		// appending its element to the `<ul>`.
		addOneMember : function(member) {
//			console.log("AppView - addOneMembers() - start");
			var view = new MemberView({
				model : member
			});

			this.$("#members").append(view.render().el);
		},

		// Add all items in the **Members** collection at once.
		addAllMemebers : function() {
//			console.log("AppView - addAllMembers() - start");
			Members.each(this.addOne);
		},

		//Create a template of the Header Row to be inserted in the AJAX call below.
		templateHeaderRow : _.template($('#member-Header-Row-tmpl').html()),

		/* Uses JAX-RS GET to retrieve current member list */
		updateMemberTable : function () {
//			console.log("AppView - Update Member - start");
			//refer to the parent function. JS is block scoped.
			var self = this;
            //Remove the table or else we will have more then one.
            $('.body').remove();
            //Clear the Collection or it will try to load every Member again.
    		Members.reset();

			var jqxhr = $.ajax({
		        url: 'rest/members',
		        cache: false,
		        type: "GET"
		    }).done(function(data, textStatus, jqXHR) {
//	            console.log("AppView - Update Member - succes on ajax call");
	        	Members.add(data);
    		}).fail(function(jqXHR, textStatus, errorThrown) {
	            console.log("AppView - Update Member - error updating table -" + error.status);
		    });
		},

        //Clear member registration and error messages on page change
        clearMessages : function(event) {
//        	console.log("AppView - clearMessages() - start");
        	$('#formMsgs').empty();
            $('span.invalid').remove();
        },

        /**
         * Attempts to register a new member using a JAX-RS POST.  The callbacks the refresh the member table, or
         * process JAX-RS response codes to update the validation errors.
         */
        submitRegistration : function(event) {
//        	console.log("AppView - submitRegistration - start");
        	//refer to the parent function. JS is block scoped.
        	var outerSelf = this;
        	
            

        	$("form#reg").submit(function(event) {
//        		console.log("AppView - submitRegistration - submit event - started");
        		var self = outerSelf;
        		event.preventDefault();
    		    //Transform the form fields into JSON.
    			var memberData = JSON.stringify(self.$("form#reg").serializeObject());
                
    		    /** The jQuery XMLHttpRequest (jqXHR) object returned by $.ajax() as of jQuery 1.5 is a superset of
    		     *   the browser's native XMLHttpRequest object. For example, it contains responseText and responseXML
    		     *   properties, as well as a getResponseHeader() method. When the transport mechanism is something
    		     *   other than XMLHttpRequest (for example, a script tag for a JSONP request) the jqXHR object
    		     *   simulates native XHR functionality where possible.
    		     *
    		     *  The jqXHR objects returned by $.ajax() as of jQuery 1.5 implement the Promise interface, giving
    		     *   them all the properties, methods, and behavior of a Promise (see Deferred object for more
    		     *   information). These methods take one or more function arguments that are called when the
    		     *   $.ajax() request terminates. This allows you to assign multiple callbacks on a single request,
    		     *   and even to assign callbacks after the request may have completed. (If the request is already
    		     *   complete, the callback is fired immediately.)
    		     */
    			var jqxhr = $.ajax({
    		        url: 'rest/members',
    		        contentType: "application/json",
    		        dataType: "json",
    		        data: memberData,
    		        type: "POST"
    		    }).done(function(data, textStatus, jqXHR) {
//		            console.log("AppView - submitRegistration - ajax done");
	    			//clear existing msgs
	    		    $('span.invalid').remove();
	    		    $('span.success').remove();
		            //clear input fields
		            $('#reg')[0].reset();

		            //mark success on the registration form
		            $('#formMsgs').append($('<span class="success">Member Registered</span>'));

		            //There is no need to clear the Collection or the 'table' as this add() will add to the existing
		            // Collection which will invoke the View and add to the 'table'.
		        	Members.add(data);
//		            console.log("AppView - submitRegistration - ajax done - after Members.add call");
        		}).fail(function(jqXHR, textStatus, errorThrown) {
	    			//clear existing  msgs
	    		    $('span.success').remove();

		            if ((jqXHR.status === 409) || (jqXHR.status === 400)) {
//		                console.log("AppView - submitRegistration - error in ajax - Validation error registering user! "
//		                		+ jqXHR.status);
		                //clear existing msgs so that when the new message is display you don't have 2 of them.
		                $('span.invalid').remove();
		                var errorMsg = $.parseJSON(jqXHR.responseText);

		                $.each(errorMsg, function(index, val) {
		                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
		                });
		            } else if (jqXHR.status === 200) {
		            	//It should not reach this error as long as the Server method returns data.
//			            console.log("AppView - submitRegistration - ajax error on 200 with error message: "
//			            		+ errorThrown.message);
//			            console.log("AppView - submitRegistration - ajax error because the REST service doesn't return" +
//			            		"any data and this app expects data.  Fix the REST app.");
			            //clear existing  msgs
			            $('span.invalid').remove();
			            //clear input fields
			            $('#reg')[0].reset();
			            //Clear the Collection or it will try to load every Member again.
			            Members.reset();
			            self.updateMemberTable();
//			            console.log("AppView - submitRegistration - ajax error on 200 - after updateMemberTable() call");
		            } else {
//			        	console.log("AppView - submitRegistration - error in ajax - " +
//			        			"jqXHR = " + jqXHR.status +
//			        			" - textStatus = " + textStatus +
//			        			" - errorThrown = " + errorThrown);
			        	//clear existing  msgs
			        	$('span.invalid').remove();
		                $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
		            }
    		    });
        	});
        },



        //Register the cancel listener
        cancelRegistration : function(event) {
//        	console.log("AppView - start cancelRegistration");
        	//clear input fields
            $('#reg')[0].reset();

            //clear existing msgs
            $('span.invalid').remove();
            $('span.success').remove();
        }

	});

	// Finally, we kick things off by creating the **App**.
	window.App = new AppView;

});

