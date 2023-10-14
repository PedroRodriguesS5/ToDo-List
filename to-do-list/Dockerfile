FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven

WORKDIR /app

COPY . .

RUN mvn clean install

FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 8080

COPY --from=build /target/to-do-list-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]