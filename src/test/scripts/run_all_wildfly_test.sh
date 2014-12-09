#!/usr/bin/env bash
: ${JBOSS_HOME?"Need to set JBOSS_HOME"}

# Workaround for stupid arquillian bug
logfile=testrun$$.log
for i in `find . -name "*IT.java"`; do
    base=$(basename $i .java) 
    mvn -Dtest=NoSuchTest -DfailIfNoTests=false -Dit.test=$base verify -Djboss.home=$JBOSS_HOME -Pwildfly-managed-integration-tests | grep -e "^Running " -e "^Tests run:" >> $logfile
done

echo Summary in $logfile
