#!/bin/bash

#Consolen-Aufruf:
#while true; do for f in "./server.log.*"; do rm $f || echo "deletion failed"; echo > $f; sleep 1m; done; done
while true;
do
for f in $1"/server.log.*";
do
rm $f || echo "deletion failes";
echo > $f;
sleep 1m;
done;
done
