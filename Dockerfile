FROM ubuntu:16.04

ARG SENDGRID_API_KEY
ARG SENDGRID_SENDER_EMAIL

RUN apt-get update && apt-get install -y default-jdk

ENV JAVA_HOME=/usr/lib/jvm/default-java
ENV SENDGRID_API_KEY=${SENDGRID_API_KEY}
ENV SENDGRID_SENDER_EMAIL=${SENDGRID_SENDER_EMAIL}

RUN mkdir -p /tmp/emailservices && mkdir -p /app/emailservices
COPY . /tmp/emailservices
WORKDIR /tmp/emailservices
RUN chmod +x ./gradlew && ./gradlew clean build
RUN mv ./build/libs/emailservices-0.0.1-SNAPSHOT.jar /app/emailservices/emailservices.jar
WORKDIR /tmp
RUN rm -rf /tmp/emailservices

EXPOSE 8080

CMD java -jar /app/emailservices/emailservices.jar
