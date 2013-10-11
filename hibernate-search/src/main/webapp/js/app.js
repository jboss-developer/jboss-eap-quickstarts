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
