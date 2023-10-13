FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

WORKDIR /app

COPY . .

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven


FROM openjdk:17-jdk-slim

EXPOSE 8080

WORKDIR /app

COPY --from=build /to-do-list/target/to-do-list-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
