FROM ubuntu:14.10
MAINTAINER Koenig Hotze

# WORKDIR /opt/tmp

ENV REFRESHED_AT 20150119
RUN apt-get update
RUN apt-get -y install wget openjdk-8-jdk
RUN apt-get -y install curl
RUN apt-get -y install libaio1

ENV HOME /home/wildfly
RUN useradd  -d $HOME -m -U wildfly
USER wildfly:wildfly

# # Set the WILDFLY_VERSION env variable
ENV WILDFLY_VERSION 8.2.0.Final
ENV TMP /tmp

WORKDIR $HOME

RUN wget -q http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz

RUN tar xzf wildfly-$WILDFLY_VERSION.tar.gz
ENV JBOSS_HOME $HOME/wildfly-$WILDFLY_VERSION

ENV LOG_VOLUME $HOME/logs
ENV DEPLOYMENT_VOLUME $HOME/deployments

VOLUME [$DEPLOYMENT_VOLUME, $LOG_VOLUME]

ADD runjboss.sh $HOME/runjboss.sh
ADD standalone-full.xml $JBOSS_HOME/standalone/configuration/standalone-full.xml

# # Jboss Ports, web and admin
EXPOSE 8080 9990

# TODO: add ear

CMD ["/home/wildfly/runjboss.sh"]
