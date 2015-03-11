FROM ubuntu:14.10
MAINTAINER Koenig Hotze

ENV REFRESHED_AT 20150305

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
RUN echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk 
RUN apt-get install -y mongodb-org=2.6.1 mongodb-org-server=2.6.1 mongodb-org-shell=2.6.1 mongodb-org-mongos=2.6.1 mongodb-org-tools=2.6.1


ENV HOME /home/mongod
RUN useradd  -d $HOME -m -U mongod
USER mongod:mongod

RUN mkdir /home/mongod/logs
# RUN mkdir /home/mongod/data
RUN mkdir -p /home/mongod/data/db

ADD mongod.conf /home/mongod/mongod.conf

ENV LOG_VOLUME /home/mongod/logs

VOLUME [$LOG_VOLUME] 

CMD ["mongod", "--config", "/home/mongod/mongod.conf", "--rest" ]

# # Mongo Ports
EXPOSE 27017 28017
