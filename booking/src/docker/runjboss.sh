#!/bin/bash
$JBOSS_HOME/bin/standalone.sh -Djboss.server.log.dir=/home/wildfly/logs -Dmongo.host=KoenighotzeMongo -Dmongo.port=${KOENIGHOTZEMONGO_PORT_27017_TCP_PORT} -c standalone-full.xml -b 0.0.0.0
