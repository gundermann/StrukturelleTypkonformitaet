#!/bin/bash

#create SignaturMatching jar
echo "Go to /d/MT/Quellcode/Signature\ Matching/bin"
cd /d/MT/Quellcode/Signature\ Matching/bin
echo "create jar"
jar cf sigmang.jar ./*
cp -f sigmang.jar /d/workspace/profilcs/extlib/common/sigmang.jar
cp -f sigmang.jar /d/workspace/profilcs/dist/jboss/standalone/deployments/deg.ear/lib/sigmang.jar
rm sigmang.jar

#create ComponentTester jar
echo "Go to /d/MT/Quellcode/ComponentTester/bin"
cd /d/MT/Quellcode/ComponentTester/bin
echo "create jar"
jar cf ctng.jar ./*
cp -f ctng.jar /d/workspace/profilcs/extlib/common/ctng.jar
cp -f ctng.jar /d/workspace/profilcs/dist/jboss/standalone/deployments/deg.ear/lib/ctng.jar
rm ctng.jar

#create DesiredComponentSourcer jar
echo "Go to /d/MT/Quellcode/DesiredComponentSourcerer/bin"
cd /d/MT/Quellcode/DesiredComponentSourcerer/bin
echo "create jar"
jar cf descosng.jar ./*
cp -f descosng.jar /d/workspace/profilcs/extlib/common/descosng.jar
cp -f descosng.jar /d/workspace/profilcs/dist/jboss/standalone/deployments/deg.ear/lib/descosng.jar
rm descosng.jar

# zusaetzlich muessen die Bibliotheken aus dem EJB-Container-Dummy kopiert werden
echo "copy junit4 from EJB-Container-Dummy"
#cp -f /d/MT/Quellcode/EJB-Container-Dummy/lib/junit4.jar /d/workspace/profilcs/extlib/common/junit4.jar
cp -f /d/MT/Quellcode/EJB-Container-Dummy/lib/junit4.jar /d/workspace/profilcs/dist/jboss/standalone/deployments/deg.ear/lib/junit4.jar

echo "copy easymock-3.0 from EJB-Container-Dummy"
#cp -f /d/MT/Quellcode/EJB-Container-Dummy/lib/easymock-3.0.jar /d/workspace/profilcs/extlib/common/easymock-3.0.jar
cp -f /d/MT/Quellcode/EJB-Container-Dummy/lib/easymock-3.0.jar /d/workspace/profilcs/dist/jboss/standalone/deployments/deg.ear/lib/easymock-3.0.jar

echo "copy hamcrest-all-1.3 from EJB-Container-Dummy"
#cp -f /d/MT/Quellcode/EJB-Container-Dummy/lib/hamcrest-all-1.3.jar /d/workspace/profilcs/extlib/common/hamcrest-all-1.3.jar
cp -f /d/MT/Quellcode/EJB-Container-Dummy/lib/hamcrest-all-1.3.jar /d/workspace/profilcs/dist/jboss/standalone/deployments/deg.ear/lib/hamcrest-all-1.3.jar