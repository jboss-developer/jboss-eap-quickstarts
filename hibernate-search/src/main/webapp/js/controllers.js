/** 
   * JBoss, Home of Professional Open Source
   * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
   * contributors by the @authors tag. See the copyright.txt in the 
   * distribution for a full listing of individual contributors.
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   * http://www.apache.org/licenses/LICENSE-2.0
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,  
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   */

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
