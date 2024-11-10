FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/vehicle-service-0.0.1-SNAPSHOT.jar vehicle-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "vehicle-service.jar"]
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
