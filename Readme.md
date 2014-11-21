JEE7 Demo and Quickstart
========================

This project shows some basic JEE7 technologies using a simple guestbook use case.

Open Shift
----------
An open shift application hosts this tool: http://jee7hotel-koenighotze.rhcloud.com

The respective jenkins is located at http://jenkins-koenighotze.rhcloud.com/job/jee7hotel-build/


Preconditions
-------------

One goal was to demonstrate building an App using only JEE7 and JDK8 at runtime, no other external dependencies are used.

Testing however uses based on Mockito and Arquillian.


Overview
--------

This application demonstrates key JEE7 features and is used as the basis for a 
tutorial/course.

The main fictional background is a pseudo Hotel Reservation Frontend used by receptionist.

Key technologies include:

* JSF 2.2
* CDI 1.1
* EJB 3.2
* JPA 2.1
* Batch
* JMS 2.0
* Arquillian for integration testing
* WebSockets
* Bootstrap
* Selenium for UI testing
* SASS 

The todo list includes (among other stuff):

* Thymeleaf
* Docker or such
* AngularJS Demo
* Generating reports/graphs using d3.js
* load testing loader.io
* Authentication using OAuth
* REST Service for Url lookup


Pseudo use cases are chosen only to demonstrate certain technologies, obviously not all are implemented yet:

* CRUD Frontends for Reservations, Guests, Rooms (CLERK role only)
* Import and export for administrators (via REST, ADMINISTRATOR role only)
* CRUD for clerks for administrator (via REST, ADMINISTRATOR, role only)
* Confirmation of reservation by backend service (JMS 2.0)
* Daily report of bookings (via batch)
* Auto-update frontends (via WebSocket)

How to build and run
--------------------

In short: 

```
mvn clean package wildfly:run
```

Then open browser at http://localhost:8080/jee7hotel

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

or 

```
./src/test/scripts/run_wildfly_test.sh
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
* JSF Flows

Business layer
--------------

* REST
* WebSocket

Domain layer
------------

* Basic JPA
* Criteria API
* Events
* PostTransaction Events
* EntityListener


Testing
-------
* Arquillian


Things to do
------------

* Security
* IIOP
* Stored Procedures
* DeltaSpike
* WeldSE, CDIUnit
* Angular JS Frontend
* JMS 2.0 
* Role based security
* Role ```**```
* ```@Delegate```

* Futures
* Embedded container ```EJBContainer.createEJBContainer```
