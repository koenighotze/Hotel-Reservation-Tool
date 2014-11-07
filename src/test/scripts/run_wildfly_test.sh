#!/usr/bin/env bash
: ${JBOSS_HOME?"Need to set JBOSS_HOME"}
mvn clean integration-test verify -Djboss.home=$JBOSS_HOME -Pwildfly-managed-integration-tests
