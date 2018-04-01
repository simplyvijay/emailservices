FROM ubuntu:16.04

ARG SMTP_SERVER
ARG EMAIL_USERNAME
ARG EMAIL_PASSWORD

RUN apt-get update && apt-get install -y default-jdk

ENV JAVA_HOME=/usr/lib/jvm/default-java
ENV SMTP_SERVER=${SMTP_SERVER}
ENV EMAIL_USERNAME=${EMAIL_USERNAME}
ENV EMAIL_PASSWORD=${EMAIL_PASSWORD}

RUN mkdir -p /tmp/emailservices && mkdir -p /app/emailservices
COPY . /tmp/emailservices
WORKDIR /tmp/emailservices
RUN chmod +x ./gradlew && ./gradlew clean build
RUN mv ./build/libs/emailservices-0.0.1-SNAPSHOT.jar /app/emailservices/emailservices.jar
WORKDIR /tmp
RUN rm -rf /tmp/emailservices

EXPOSE 8080

CMD java -jar /app/emailservices/emailservices.jar
