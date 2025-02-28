FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/client-service-0.0.1-SNAPSHOT.jar client-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "client-service.jar"]