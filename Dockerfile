FROM openjdk:21-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} ugur_backend-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

LABEL authors="nury"

