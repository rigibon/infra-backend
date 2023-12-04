# syntax = docker/dockerfile:1.2
#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
COPY pom.xml .
# RUN mvn clean package assembly:single -DskipTests
RUN mvn clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target
COPY src ./src
RUN mvn clean package -Dmaven.test.skip

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/javalin-deploy-1.0-SNAPSHOT-jar-with-dependencies.jar libros.jar
# ENV PORT=8080
EXPOSE 8080
CMD ["java","-classpath","libros.jar","ar.edu.dds.libros.AppLibros"]
