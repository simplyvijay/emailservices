#!/bin/bash

if [ -z "$SMTP_SERVER" ]; then
echo "SMTP_SERVER is not defined"
exit 1
fi

if [ -z "$EMAIL_USERNAME" ]; then
echo "EMAIL_USERNAME is not defined"
exit 1
fi

if [ -z "$EMAIL_PASSWORD" ]; then
echo "EMAIL_PASSWORD is not defined"
exit 1
fi

export EMAILSERVICES_HOME=`pwd`

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