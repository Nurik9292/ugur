FROM  eclipse-temurin:21-jdk as builder
WORKDIR /app
COPY . /app
RUN ./mvnw package -DskipTests
RUN mv -f target/*.jar app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/app.jar .
ENTRYPOINT ["java", "-jar", "app.jar"]

LABEL authors="nury"

