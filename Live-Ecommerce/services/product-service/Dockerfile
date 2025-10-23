# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jre AS run
WORKDIR /app
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
