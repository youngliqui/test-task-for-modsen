FROM gradle:8.9-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
