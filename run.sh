#!/bin/bash

export EMAILSERVICES_HOME=`pwd`
echo EMAILSERVICES_HOME=`pwd` > tempfile
cat /home/emailservices/envtemplate tempfile > /home/emailservices/envfile

echo 'Building the project'
chmod +x ./gradlew
./gradlew clean build

echo 'Stopping the existing service'
service emailservices stop
sleep 10
rm /etc/systemd/system/emailservices.service

echo 'Starting the service'
cp $EMAILSERVICES_HOME/deployment/emailservices.service /etc/systemd/system/emailservices.service
systemctl daemon-reload
service emailservices start