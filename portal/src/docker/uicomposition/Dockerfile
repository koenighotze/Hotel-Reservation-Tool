FROM nginx
MAINTAINER Koenig Hotze

# WORKDIR /opt/tmp

ENV REFRESHED_AT 20151006
#RUN apt-get update
#RUN apt-get -y install wget openjdk-8-jdk
#RUN apt-get -y install curl
#RUN apt-get -y install libaio1

COPY content /usr/share/nginx/html
COPY conf /etc/nginx

VOLUME [/usr/share/nginx/html, /etc/nginx]