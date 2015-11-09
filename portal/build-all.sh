#!/bin/bash
pushd .. && mvn package && find . -name "*war" -type f -exec cp -v {} portal/src/docker/asset/ \; && popd && cd src/docker && docker build -t koenighotze/jee7hotelportal .
