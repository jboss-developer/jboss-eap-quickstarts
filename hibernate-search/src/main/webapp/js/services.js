'use strict';

/* Services */

/*angular.module('feedAppServices', ['ngResource']).
    factory('Feed', function($resource){
  return $resource('feeds/:id.json', {}, {
    query: {method:'GET', params:{id:'feeds'}, isArray:true}
  });
});*/

angular.module('feedAppServices', ['ngResource']).
    factory('Feed', function($resource){
        return $resource('rest/feedReader/feeds/:id', {}, {
            query: {method:'GET', params:{id:'feeds'}, isArray:true}
        });
    });
