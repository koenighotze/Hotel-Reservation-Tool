#!/bin/bash
#docker run --rm=true --name=jee7hotelportal -it -p 9995:9990 -p 80:8080 --env DOCKER_MACHINE_IP=$(docker-machine ip default)  --link jee7hotelguest --link jee7hotelbooking --link jee7hotelfacilities koenighotze/jee7hotelportal
docker run --rm=true --name=jee7hotelportal -it -p 9995:9990 -p 80:8080 --env DOCKER_MACHINE_IP=$(docker-machine ip default)  koenighotze/jee7hotelportal
