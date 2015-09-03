#!/usr/bin/env bash
: ${JBOSS_HOME?"Need to set JBOSS_HOME"}
mvn $* verify -Djboss.home=$JBOSS_HOME -Pwildfly-remote-integration-tests 
