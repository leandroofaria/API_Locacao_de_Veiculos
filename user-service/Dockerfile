# Etapa de build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Etapa de execução
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "user-service.jar"]


