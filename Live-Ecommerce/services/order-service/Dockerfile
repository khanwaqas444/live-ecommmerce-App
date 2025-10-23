# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jre AS run
WORKDIR /app
COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
