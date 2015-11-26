mvn package

for i in booking facilities guest portal; do
    pushd $i && mvn docker:build && popd
done
