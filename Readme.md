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


How to run integration tests
----------------------------


###Wildfly
Remember to set ```-Djboss.home``` or similar!

```
mvn -o clean integration-test verify  -Djboss.home= -Pwildfly-managed-integration-tests
```

###Glassfish
```
mvn clean integration-test verify -Pglassfish-integration-tests
```

Introduction
------------

###The use case: Guest Book


###Basic application architecture


Presentation layer
------------------
* Basic JSF 2.2
* Ajax
* HTML 5 integration
* Flows


Business layer
--------------
* Security
* REST
* IIOP
* WebSocket

Domain layer
------------
* Basic JPA
* Criteria API
* Stored Procedures
* Events
* PostTransaction Events
* EntityListener


Testing
-------
* Arquillian
* DeltaSpike
* WeldSE, CDIUnit

