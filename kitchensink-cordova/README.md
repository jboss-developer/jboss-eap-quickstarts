kitchensink-cordova: Example Mobile Application Using Hybrid HTML5 + REST with Apache Cordova
===============================================================================================
Author: Kris Borchers
Level: Intermediate
Technologies: HTML5, REST, Apache Cordova
Summary: Based on kitchensink, but uses hybrid HTML5 running as a native application on mobiles
Target Product: WFK

What is it?
-----------

This project serves as an example of the HTML5 [kitchensink quickstart](https://github.com/jboss-jdf/jboss-as-quickstart/tree/master/kitchensink-html5-mobile), converted to an [Apache Cordova](http://cordova.apache.org/) based, hybrid application.

What does all of that mean? Basically, this takes our [HTML5 + REST](https://community.jboss.org/wiki/HTML5RESTApplications) / [jQuery Mobile](http://jquerymobile.com/) based web app and converts it to a native app for both iOS and Android. Currently, these apps will need to be built separately but in the future we hope to provide a single build step for all supported mobile OS types. These concepts can be applied to the conversion of most HTML5/JS based web apps by just replacing the specific paths described in this article with paths that match your environment.

Available Hybrid Applications
-----------------------------

The following is a list of the currently available hybrid applications.
For more detailed information about a quickstart, click on the quickstart name.

This quickstart contains the following mobile OS types:

1. [ios](ios/README.md)
2. [android](android/README.md)
