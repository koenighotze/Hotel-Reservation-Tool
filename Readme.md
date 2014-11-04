JEE7 Demo and Quickstart
========================

This project shows some basic JEE7 technologies using a simple guestbook use case.

Overview
--------

How to build and run
--------------------
```
mvn clean install
```


###Embedded Glassfish

Some features may not work, unless the JSF lib contained in glassfish is overriden.
```
mvn embedded-glassfish:run
```


###Embedded Wildfly

```
mvn wildfly:run

mvn wildfly:deploy -Dforce=true
```

Introduction
------------

###The use case: Guest Book


###Basic application architecture


Presentation layer
------------------
[ ] Basic JSF 2.2
[ ] Ajax
[ ] HTML 5 integration
[ ] Flows


Business layer
--------------
[ ] Security
[ ] REST
[ ] IIOP
[ ] WebSocket

Domain layer
------------
[ ] Basic JPA
[ ] Criteria API
[ ] Stored Procedures
[ ] Events, PostTransaction Events



Testing
-------
[ ] Arquillian
[ ] DeltaSpike
[ ] WeldSE, CDIUnit
mvn -o clean integration-test verify -Djboss.home=/Users/dschmitz/dev/java/javaee7_tutorial/wildfly-8.1.0.Final/  
mvn clean integration-test -Djboss.home=/Users/dschmitz/dev/java/javaee7_tutorial/wildfly-8.1.0.Final/ -Pglassfish-integration-tests
