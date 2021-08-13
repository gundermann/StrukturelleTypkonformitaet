#!/bin/bash

#Consolen-Aufruf:
#i=0; while true; do cp tmp_KOFGPCProvider.log /e/MT/logs/tmp_KOFGPCProvider"$(($i+1))"log.txt; echo > tmp_KOFGPCProvider.log; i=$i+1; sleep 1h; done
i=0;
while true;
do
cp $1/tmp_KOFGPCProvider.log $2/tmp_KOFGPCProvider"$(($i+1))"log.txt;
echo > $1/tmp_KOFGPCProvider.log;
i=$i+1;
sleep $3m;
done
