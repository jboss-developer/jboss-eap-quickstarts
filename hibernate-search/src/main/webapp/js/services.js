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