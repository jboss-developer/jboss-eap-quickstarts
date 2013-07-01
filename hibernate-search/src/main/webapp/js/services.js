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
            query: {method: 'GET', isArray: true}
        });
    }).factory('FeedItem', function ($resource) {
        return $resource('rest/feedReader/feeds/:id', {}, {
        })
    });
