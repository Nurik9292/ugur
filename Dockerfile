FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine

#ARG JAR_FILE=target/*.jar

WORKDIR /app

#COPY ${JAR_FILE} app/app.jar

COPY --from=builder /app/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app/app.jar"]

LABEL authors="nury"

