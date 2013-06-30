'use strict';

/* App Module */

angular.module('feedApp', ['feedAppFilters', 'feedAppServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/feeds', {templateUrl: 'partials/feed-list.html',   controller: FeedListCtrl}).
      when('/feeds/:id', {templateUrl: 'partials/feed-detail.html', controller: FeedDetailCtrl}).
      otherwise({redirectTo: '/feeds'});
}]);
