'use strict';

/* Controllers */

function FeedListCtrl($scope, FeedCollection) {
  $scope.feeds = FeedCollection.query();
  $scope.orderProp = 'age';
}

//FeedListCtrl.$inject = ['$scope', 'FeedCollection'];

function FeedDetailCtrl($scope, $routeParams, FeedCollection,FeedItem) {
  /*$scope.feed = FeedCollection.get({id: $routeParams.id}, function(feed) {
  });*/
  $scope.feed = FeedItem.get({id: $routeParams.id});
  /*$scope.setImage = function(imageUrl) {
  }*/
}

//FeedDetailCtrl.$inject = ['$scope', '$routeParams', 'FeedCollection'];
