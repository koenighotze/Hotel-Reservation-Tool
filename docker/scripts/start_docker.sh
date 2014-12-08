#!/bin/bash
# docker run -v $HOSTDIR:$DOCKERDIR
docker run -it -p 9990:9990 jboss/wildfly-admin
