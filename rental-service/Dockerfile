FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/rental-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
