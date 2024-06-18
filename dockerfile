FROM maven:3.6.3-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml /app/
COPY src /app/src
COPY static /app/src
COPY ./server.config /app

RUN mvn clean package

FROM openjdk:23-ea-17-jdk-bullseye

WORKDIR /app

# Copy the JAR file from the first stage
COPY --from=build /app/target/* /app/target/
COPY --from=build /app/server.config /app/

# Expose port (if your app uses a specific port)
EXPOSE 2024

ENTRYPOINT ["java", "-cp", "target/HTTPServer-1.0-SNAPSHOT.jar", "MK.HTTPServer.App"]

