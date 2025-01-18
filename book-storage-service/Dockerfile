FROM gradle:8.9-jdk21 AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/book-storage-service-0.0.1-SNAPSHOT.jar /app/book-storage-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "book-storage-service-0.0.1-SNAPSHOT.jar"]
