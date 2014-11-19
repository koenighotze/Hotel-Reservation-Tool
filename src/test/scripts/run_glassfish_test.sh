#!/usr/bin/env bash
: ${$GLASSFISH_HOME?"Need to set $GLASSFISH_HOME"}
mvn $* verify -DglassFishHome=$GLASSFISH_HOME -Pglassfish-managed-integration-tests
