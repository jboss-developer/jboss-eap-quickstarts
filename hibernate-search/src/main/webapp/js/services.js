'use strict';

/* Services */

/*angular.module('feedAppServices', ['ngResource']).
    factory('FeedCollection', function($resource){
  return $resource('feeds/:id.json', {}, {
    query: {method:'GET', params:{id:'feeds'}, isArray:true}
  });
});*/

angular.module('feedAppServices', ['ngResource']).
    factory('FeedCollection', function($resource){
        return $resource('rest/feedReader/feeds', {}, {
            query: {method: 'GET', isArray: true},
            save:  {method:'POST',params:{Feed:'newFeed'}}
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
        })
    });
