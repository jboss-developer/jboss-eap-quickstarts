kitchensink-cordova: Example Mobile Applications Using Hybrid HTML5 + REST with Apache Cordova
===============================================================================================
Author: Kris Borchers
Level: Intermediate
Technologies: HTML5, REST, Apache Cordova
Summary: Based on kitchensink, but uses hybrid HTML5 running as a native application on mobiles
Target Product: WFK

What is it?
-----------

This project serves as an example of the HTML5 [kitchensink](https://github.com/jboss-jdf/jboss-as-quickstart/tree/master/kitchensink-html5-mobile) quickstart converted to a hybrid [Apache Cordova](http://cordova.apache.org/) application.

What does this mean? Basically, this takes our [HTML5 + REST](https://community.jboss.org/wiki/HTML5RESTApplications) / [jQuery Mobile](http://jquerymobile.com/) web application and converts it to a native application for both iOS and Android. 

Currently, these applications must be built separately, but in the future we hope to provide a single build step for all supported mobile OS types. 

These concepts can be applied to the conversion of most HTML5/JS based web apps by just replacing the specific paths described in this article with paths that match your environment. If you have an existing Web Application based on HTML5 + REST, use the following guide to convert it to a hybrid application with Apache Cordova: [Converting a Mobile HTML5 + REST Web App to a Hybrid App with Apache Cordova](http://aerogear.org/docs/guides/HTML5ToHybridWithCordova/)



Available Hybrid Applications
-----------------------------

The following is a list of the currently available hybrid applications.
For more detailed information about a quickstart, click on the quickstart name.

Click on the link below to see instructions to see instructions for each OS type:

1. [Kitchensink Apache Cordova iOS](#kitchensink-apache-cordova-ios-example)
2. [Kitchensink Apache Cordova Android](#kitchensink-apache-cordova-android-example)



Kitchensink Apache Cordova iOS Example
-----------------------------------

### What is it?

This quickstart was built upon the HTML5 kitchensink quickstart, which was converted to a hybrid Apache Cordova application. Basically, this takes our HTML5 + REST / jQuery Mobile based web application and converts it to a native application for iOS.

### System requirements

All you need to build this project is Mac OS X Lion (10.7) (or later) and XCode 4.5 or later and the appropriate iOS SDK for your needs. If you need more detailed instruction to setup a iOS Development Environment with Apache Cordova, take a look at [Setting up your development environment to use Apache Cordova](http://aerogear.org/docs/guides/CordovaSetup/)


### Import the Quickstart Code

First you need to import the existing iOS code to XCode.

1. Open Finder and navigate to `QUICKSTART_HOME/kitchensink-cordova/ios/`. Be sure to replace `QUICKSTART_HOME` with the root directory of your quickstarts.
2. Right click on *KitchensinkCordova.xcodeproj* and select *Open With XCode*
3. Change the `Target` in the Scheme menu to KitchensinkCordova and select a device
4. Make sure that your `QUICKSTART_HOME/kitchensink-cordova/ios/www` is a symbolic link to `../shared/www`


### Run and Access the Application

If the toolbar in XCode is visible, click on the `Run` button. This will start the iOS Simulator with this quickstart running in it. If your toolbar is not visible, click `View -> Show Toolbar` to show it.


Kitchensink Apache Cordova Android Example
-----------------------------------


### What is it?

This application is built upon the HTML5 kitchensink quickstart, which was converted to an Apache Cordova based hybrid application. Basically, this takes our HTML5 + REST / jQuery Mobile based web application and converts it to a native application for Android. 

### System requirements

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, JBDS 5 or Eclipse with [Android Development Tools (ADT) Plugin for the Eclipse IDE](http://developer.android.com/tools/sdk/eclipse-adt.html). If you need more detailed instruction to setup a Android Development Environment with Apache Cordova, you can take a look at [Setting up your development environment to use Apache Cordova](http://aerogear.org/docs/guides/CordovaSetup/)

### Import the Quickstart Code

First we need to import the existing Android code to JBDS or Eclipse

1. In Eclipse, click on `File`, then `Import`.
2. Select `Existing Android Code Into Workspace` and click `Next`.
3. In `Root Directory`, click on the `Browse...` button, then find and select `QUICKSTART_HOME/kitchensink-cordova/android/`. Be sure to replace `QUICKSTART_HOME` with the root directory of your quickstarts.
4. Click the `Finish` button to start the project import.
5. Make sure that your `QUICKSTART_HOME/kitchensink-cordova/android/assets/www` is a symbolic link to `../shared/www`


### Troubleshooting Tips

#### Troubleshooting Fedora/Red Hat Enterprise Linux

If you receive an error _Unable to resolve target 'android-10'_, it means you don't have the Android API 10 (Android 2.3.3) installed. 

To solve this you must:

1. Right-click the project in the left pane.
2. Select `Properties`
3. In Android options, select the appropriate Android API.

_NOTE: This sample was built for the Android 2.1 SDK for maximum compatibility with current devices but should work on any 2.x or 4.0 SDK. The sample was not tested on the 3.x series._

#### Troubleshooting Windows Operating Systems

As Windows doesn't support symbolic links you must copy `shared/www` folder to `QUICKSTART_HOME/kitchensink-cordova/android/assets/www`


### Start the Emulator and Deploy the application

1. To start the emulator in Eclipse, click on `Window` and select `AVD Manager`.
2. In the `Android Virtual Device Manager` window, select the appropriate AVD and click the `Start` button.
3. In the `Launch Options` window. click the `Launch` button.
4. When the Emulator starts, select your project on Eclipse.
5. Click on `Run`, then `Run As` and `Android Application`.

### Access the application

The application will be running on the Emulator.




