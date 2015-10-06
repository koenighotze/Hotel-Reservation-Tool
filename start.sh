#!/bin/bash

# Convinience script for starting the whole application w/o docker

mvn install || exit

pushd booking

(mvn wildfly:run) &

# wait for wildfly to come up and running
while true; do
    if curl --output /dev/null --silent --head --fail "http://localhost:8080/booking"; then
      break
    else
        echo "Waiting for Wildfly..."
        sleep 1
    fi
done

popd

for i in guest facilities portal; do
    pushd $i
    mvn wildfly:deploy 
    popd 
done


echo "All apps are deployed"
echo " * http://localhost:8080/portal"
echo " * http://localhost:8080/guest"
echo " * http://localhost:8080/facilities"
echo " * http://localhost:8080/booking"
echo "Use mvn wildfly:shutdown to stop Wildfly"

