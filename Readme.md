# JEE7 Demo and Quickstart

This project shows some basic JEE7 technologies using a simple guestbook use case.

## Quick Start

This project consists of three applications, each is constraint to a clear domain:

* booking
* guest
* facilities

Furthermore, a fourth application - portal - integrates the other three into a single
UI.

Just run the following to start up all apps:

```
# cd <WHEREEVER_THE_CODE_ROOT_IS>
# ./start.sh
```

Manually, you just need to cd into the app you want to start and run `mvn wildfly:run`
This will startup the application using an embedded WildFly. 


## Overview of the architecture
TODO


This application demonstrates key JEE7 features and is used as the basis for a
tutorial/course.

The main fictional background is a pseudo Hotel Reservation Frontend used by receptionist.

### Use cases and user stories
Pseudo use cases are chosen only to demonstrate certain technologies, obviously not all are implemented yet:

* CRUD Frontends for Reservations, Guests, Rooms (CLERK role only)
* Import and export for administrators (via REST, ADMINISTRATOR role only)
* CRUD for clerks for administrator (via REST, ADMINISTRATOR, role only)
* Confirmation of reservation by backend service (JMS 2.0)
* Daily report of bookings (via batch)
* Auto-update frontends (via WebSocket)

### Tech stack

One goal was to demonstrate building an App using only JEE7 and JDK8 at runtime, no other external dependencies are used.

Testing however uses based on Mockito and Arquillian.

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


## Todo list
The todo list includes (among other stuff):

* Thymeleaf
* AngularJS Demo
* Generating reports/graphs using d3.js
* load testing loader.io
* Authentication using OAuth (google+ only)
* REST Service for Url lookup
* docker compose for setting up containers


# ye olde docs

### Open Shift (Deprecated...for now)
An open shift application hosts this tool: http://jee7hotel-koenighotze.rhcloud.com

The respective jenkins is located at http://jenkins-koenighotze.rhcloud.com/job/jee7hotel-build/


=== Long version

* Start mongo docker

* Start wildfly docker

* determine host and port



```
docker ps
CONTAINER ID        IMAGE                               COMMAND                CREATED             STATUS              PORTS                                                NAMES
cb004f78965c        koenighotze/wildfly:latest          "bash"                 5 minutes ago       Up 4 minutes        0.0.0.0:49185->8080/tcp, 0.0.0.0:49186->9990/tcp     KoenighotzeWildfly  

```


```
boot2docker ip -> 192.168.59.103
```

* open http://192.168.59.103:49185/jee7hotel/


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

###The use case: Booking Portal for Room Reservation

* Application for receptionist
* Mobile page for client-booking-checks


###Basic application architecture

---------------------------------------------------------------------
  digraph G {
      node [fontname=Verdana,fontsize=12];
      node [style=filled];
      node [fillcolor="#EEEEEE"];
      node [color="#EEEEEE"];
      edge [color="#31CEF0"];

      Web_Frontend -> Service_Layer -> MySqlDB;
      ServiceLayer -> MongoDB;
      Mobile_Frontend -> Service_Layer;
  }
  ----



Docker
------

# Image Overview

This section explains the structure and setup of the different docker images.

The following images are used for operating the system:

1. mongod (`docker/builds`)
2. wildfly (``)
3. mysql (``)


The following figure illustrates the depencies between said containers.

---------------------------------------------------------------------
digraph G {
    node [fontname=Verdana,fontsize=12];
    node [style=filled];
    node [fillcolor="#EEEEEE"];
    node [color="#EEEEEE"];
    edge [color="#31CEF0"];

    wildfly -> mongod[label="link"];
    wildfly -> mysql[label="link"];

    wildfly -> logs[label="volume"];
    wildfly -> deployment[label="volume"];  
}
----

## Building the images from scratch

TODO


## Wildfly

## Mongo


* start image using

```
docker run --rm -i -t -P  -v $LOG_VOLUME:/home/mongodb/logs/ --name KoenighotzeMongo koenighotze/jee7hotelmongo
```

* determine port

```
docker ps
CONTAINER ID        IMAGE                               COMMAND                CREATED             STATUS              PORTS                                                NAMES
6884f24081b6        koenighotze/jee7hotelmongo:latest   "mongod --config /ho   46 seconds ago      Up 44 seconds       0.0.0.0:49169->27017/tcp, 0.0.0.0:49170->28017/tcp   KoenighotzeMongo  
```

* connect to mongo

```
mongo $(docker-ip):49169
```


## MySql



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

* Arquillian UI
* Security
* IIOP
* Stored Procedures
* Angular JS Frontend
* JMS 2.0
* Role based security
* Role ```**```
* ```@Delegate```
* add HAL to rest services

* Futures
* Embedded container ```EJBContainer.createEJBContainer```
* Check why wildfly 9 breaks flows
