#!/bin/bash

#create SignaturMatching jar
echo "Go to /c/Users/ngundermann/Desktop/MA/Repo/StrukturelleTypkonformitaet/Quellcode/Signature\ Matching/bin"
cd /c/Users/ngundermann/Desktop/MA/Repo/StrukturelleTypkonformitaet/Quellcode/Signature\ Matching/bin
echo "create jar"
jar cf sigmang.jar ./*
cp -f sigmang.jar /d/workspace/profil/extlib/common/sigmang.jar
cp -f sigmang.jar /d/workspace/profil/dist/jboss/standalone/deployments/deg.ear/lib/sigmang.jar
rm sigmang.jar

#create ComponentTester jar
echo "Go to /c/Users/ngundermann/Desktop/MA/Repo/StrukturelleTypkonformitaet/Quellcode/ComponentTester/bin"
cd /c/Users/ngundermann/Desktop/MA/Repo/StrukturelleTypkonformitaet/Quellcode/ComponentTester/bin
echo "create jar"
jar cf ctng.jar ./*
cp -f ctng.jar /d/workspace/profil/extlib/common/ctng.jar
cp -f ctng.jar /d/workspace/profil/dist/jboss/standalone/deployments/deg.ear/lib/ctng.jar
rm ctng.jar

#create DesiredComponentSourcer jar
echo "Go to /c/Users/ngundermann/Desktop/MA/Repo/StrukturelleTypkonformitaet/Quellcode/DesiredComponentSourcerer/bin"
cd /c/Users/ngundermann/Desktop/MA/Repo/StrukturelleTypkonformitaet/Quellcode/DesiredComponentSourcerer/bin
echo "create jar"
jar cf descosng.jar ./*
cp -f descosng.jar /d/workspace/profil/extlib/common/descosng.jar
cp -f descosng.jar /d/workspace/profil/dist/jboss/standalone/deployments/deg.ear/lib/descosng.jar
rm descosng.jar