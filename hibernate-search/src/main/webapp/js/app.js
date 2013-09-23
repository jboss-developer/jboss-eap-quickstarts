'use strict';

/* App Module */

angular.module('feedApp', ['feedAppFilters', 'feedAppServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/feeds', {templateUrl: 'partials/feed-list.html',   controller: FeedListCtrl}).
      when('/search', {templateUrl: 'partials/feed-search.html',   controller: FeedListCtrl}).
      when('/feeds/:id', {templateUrl: 'partials/feed-detail.html', controller: FeedDetailCtrl}).
      when('/admin', {templateUrl: 'partials/admin.html', controller: AdminCtrl}).
      when('/contact', {templateUrl: 'partials/contact.html', controller: FeedListCtrl}).
      otherwise({redirectTo: '/feeds'});
}]);
