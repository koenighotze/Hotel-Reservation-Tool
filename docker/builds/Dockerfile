FROM fedora:20
MAINTAINER Koenig Hotze

# FROM jboss/base-jdk:8

WORKDIR /opt/tmp

RUN yum -y install wget
RUN wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-x64.tar.gz" 
RUN tar xzf jdk-8u25-linux-x64.tar.gz  && mv jdk1.8.0_25 /opt/jdk1.8.0_25

RUN yum -y install mongodb-server.x86_64  && mkdir -p /var/mongodb/data/db  && mkdir -p /var/mongod/log 
    #&& mongod  --fork --rest --logpath /var/mongod/log/mongo.log --dbpath /var/mongod/data/db 

RUN yum -y update && yum clean all

# Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 8.2.0.Final
ENV WILDFLY_TARGET_DIR /opt/jboss/wildfly
ENV JAVA_HOME /opt/jdk1.8.0_25

# download stuff
RUN cd $HOME  && curl http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz | tar zx && mv $HOME/wildfly-$WILDFLY_VERSION $HOME/wildfly
ENV JBOSS_HOME /opt/jboss/wildfly

# mongodb -rest

EXPOSE 8080 9990 27017 28017

# CMD ["/bin/bash"]

#ENTRYPOINT mongod  --fork --rest --logpath /var/mongod/log/mongo.log --dbpath /var/mongod/data/db

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c" ,  "standalone-full.xml", "-b", "0.0.0.0"]

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

ADD ../target/jee7hotel.war /opt/jboss/wildfly/standalone/deployments/
