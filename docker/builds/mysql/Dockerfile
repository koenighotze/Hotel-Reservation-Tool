FROM ubuntu:14.10
MAINTAINER Koenig Hotze

ENV REFRESHED_AT 20150305

#WORKDIR /opt/tmp

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
RUN echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk 
RUN apt-get install -y mongodb-org=2.6.1 mongodb-org-server=2.6.1 mongodb-org-shell=2.6.1 mongodb-org-mongos=2.6.1 mongodb-org-tools=2.6.1


#RUN apt-get imongodb-server.x86_64  && mkdir -p /var/mongod/data/db  && mkdir -p /var/mongod/log 

# # install java 
# RUN wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-x64.tar.gz" 
# RUN tar xzf jdk-8u25-linux-x64.tar.gz  && mv jdk1.8.0_25 /opt/jdk1.8.0_25

#RUN yum -y install mongodb-server.x86_64  && mkdir -p /var/mongod/data/db  && mkdir -p /var/mongod/log 
    #&& mongod  --fork --rest --logpath /var/mongod/log/mongo.log --dbpath /var/mongod/data/db 

#RUN yum -y update && yum clean all

# Set the WILDFLY_VERSION env variable
# ENV WILDFLY_VERSION 8.2.0.Final
# ENV WILDFLY_TARGET_DIR /opt/wildfly
# ENV JAVA_HOME /opt/jdk1.8.0_25
# ENV JBOSS_HOME /opt/wildfly

# # download wildfly
# RUN cd $HOME  \
#     && curl http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz | tar zx && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME
    
# RUN nohup mongod --fork --rest --logpath /var/mongod/log/mongo.log --dbpath /var/mongod/data/db &

# # Jboss Ports
# EXPOSE 8080 9990 

# # Mongo Ports
# EXPOSE 27017 28017

# ENTRYPOINT ["/opt/wildfly/bin/standalone.sh", "-c" ,  "standalone-full.xml", "-b", "0.0.0.0"]

# #CMD ["/bin/bash"]
# # RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

# # ADD ../target/jee7hotel.war /opt/jboss/wildfly/standalone/deployments/
