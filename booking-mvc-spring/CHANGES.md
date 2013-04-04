CHANGES TO SPRING PETCLINIC EXAMPLE
===================================

ORIGINAL SOURCE
---------------
[Spring BookingMVC](<https://github.com/SpringSource/spring-webflow-samples/tree/master/booking-mvc>)

CHANGES
-------

     Removed `<property name="saveOutputToFlashScopeOnRedirect" value="true"/>` from `FlowHandlerAdapter` in `webmvc-config.xml`. This is because the property was recently added in a M1 release and not yet in released in a Final Version.

     The pom.xml was changed to leverage the power of JDF-Boms, in paticular the following: jboss-javaee-6.0-with-hibernate, jboss-javaee-6.0-with-spring, and jboss-javaee-6.0-with-tools.