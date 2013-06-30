'use strict';

/* Controllers */

function FeedListCtrl($scope, Feed) {
  $scope.feeds = Feed.query();
  $scope.orderProp = 'age';
}

//FeedListCtrl.$inject = ['$scope', 'Feed'];



function FeedDetailCtrl($scope, $routeParams, Feed) {
  $scope.feed = Feed.get({id: $routeParams.id}, function(feed) {
  });

  $scope.setImage = function(imageUrl) {
  }
}

//FeedDetailCtrl.$inject = ['$scope', '$routeParams', 'Feed'];
