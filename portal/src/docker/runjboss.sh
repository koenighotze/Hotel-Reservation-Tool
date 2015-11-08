#!/bin/bash
$JBOSS_HOME/bin/standalone.sh -Djboss.server.log.dir=/home/wildfly/logs  -c standalone-full.xml -b 0.0.0.0
