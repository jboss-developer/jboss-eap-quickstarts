'use strict';

/* Controllers */

function FeedListCtrl($scope, FeedCollection,FeedItem,FeedEntryCollection) {
  $scope.feedUrl = "";
  $scope.feeds = FeedCollection.query();
  $scope.orderProp = 'age';
  $scope.feedEntries = FeedEntryCollection.query();

    $scope.addFeed = function (FeedItem) {
        alert($scope.feedUrl);
        var newFeed = {
            url : $scope.feedUrl
        };
        FeedItem.save({},newFeed);
//        $scope.feeds.push(FeedItem.save(newFeed));
        $scope.feedUrl = "";
    };
}

//FeedListCtrl.$inject = ['$scope', 'FeedCollection'];

function FeedDetailCtrl($scope, $routeParams, FeedCollection,FeedItem,FeedEntrySpecificCollection) {
  /*$scope.feed = FeedCollection.get({id: $routeParams.id}, function(feed) {
  });*/
  $scope.feed = FeedItem.get({id: $routeParams.id});
  $scope.specificFeedEntries = FeedEntrySpecificCollection.get({feedId: $routeParams.id});
  /*$scope.setImage = function(imageUrl) {
  }*/
}

//FeedDetailCtrl.$inject = ['$scope', '$routeParams', 'FeedCollection'];
