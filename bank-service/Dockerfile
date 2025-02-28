FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/bank-service-0.0.1-SNAPSHOT.jar bank-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "bank-service.jar"]