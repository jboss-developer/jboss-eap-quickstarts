'use strict';

/* Controllers */

function FeedListCtrl($scope, FeedCollection,FeedEntryCollection,FeedEntryFromIndexedCollection) {
  $scope.feedUrl = "";
  $scope.feeds = FeedCollection.query();
  $scope.orderProp = 'age';
  $scope.feedEntries = FeedEntryCollection.query();

  $scope.searchIndex = function (text) {
        $scope.feedEntries = FeedEntryFromIndexedCollection.query({text: text});
  };
}

//FeedListCtrl.$inject = ['$scope', 'FeedCollection'];

function FeedDetailCtrl($scope, $routeParams,FeedItem,FeedEntrySpecificCollection) {
  $scope.feed = FeedItem.get({id: $routeParams.id});
    $scope.specificFeedEntries = FeedEntrySpecificCollection.query({id: $routeParams.id});
}

//FeedDetailCtrl.$inject = ['$scope', '$routeParams', 'FeedCollection'];

function AdminCtrl($scope ,Feed) {

    //$scope.feeds = Feed.query();

    var createFeed = function (newFeed) {
        newFeed.$save({},
        		  // success handler
        		  function() {
        			$scope.feeds.push(newFeed);
        		    alert("Successfully added");	    
        		  },
        		  // error handler
        		  function() {
          		    alert("Invalid URL");
          		  }); 
    };

    var updateFeed = function(feed) {
        feed.$update();
    };

    $scope.showEdit = function () {
        $scope.isEditVisible = true;
        $scope.editableFeed = new Feed();
    };

    $scope.saveFeed = function (feed) {
        $scope.isEditVisible = false;
        if (feed.id) {
            updateFeed(feed);
        }
        else {
            createFeed(feed);
        }
    };

    $scope.editFeed = function (feed) {
        $scope.isEditVisible = true;
        $scope.editableFeed = feed;
    };

    $scope.deleteFeed = function (feed) {
        feed.$delete();
        //$scope.feeds = _.without($scope.feeds, feed);
    };

    $scope.isEditVisible = false;
    $scope.feeds = Feed.query();
}
