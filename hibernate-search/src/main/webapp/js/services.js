/** 
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

'use strict';

/* Services */

//angular.module("feedApp", ["feedAppServices"]);

angular.module('feedAppServices', ['ngResource']).
    factory('FeedCollection', function($resource){
        return $resource('rest/feedReader/feeds', {}, {
            query: {method: 'GET', isArray: true},
            get:     {method:'GET',  params:{id : 'id'}, isArray : false},
            save:    {method:'POST', isArray : false},
            update:  {method:'PUT',   isArray : false},
            delete:  {method:'DELETE', isArray : false}
        });
    }).factory('Feed', function($resource){
        return $resource('rest/feedReader/feeds', {}, {
            query:   {method:'GET', isArray : true},
            get:     {method:'GET',  params:{id : 'id'}, isArray : false},
            save:    {method:'POST', isArray : false},
            update:  {method:'PUT',   isArray : false},
            delete:  {method:'DELETE', isArray : false}
        });
    }).factory('FeedItem', function ($resource) {
        return $resource('rest/feedReader/feeds/:id', {}, {
        });
    }).factory('FeedEntryCollection', function ($resource) {
        return $resource('rest/feedReader/feedEntries', {}, {
            query: {method: 'GET', isArray: true}
        });
    }).factory('FeedEntrySpecificCollection', function ($resource) {
        return $resource('rest/feedReader/feedEntries/:id', {}, {
            query: {method: 'GET',params:{id:'id'}, isArray: true}
        });
    }).factory('FeedEntryFromIndexedCollection', function($resource){
        return $resource('rest/feedReader/feeds/search/:text', {}, {
            query: {method: 'GET',params:{text:'text'}, isArray: true}
        });
    });