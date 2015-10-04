#!/bin/bash

mvn install

for i in `echo *integration-test`; do
  pushd $i

  mvn test -Dtest=*IT*

  popd
done
