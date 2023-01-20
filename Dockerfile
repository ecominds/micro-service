# Dockerfile content
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} rest-api.jar
ENTRYPOINT ["java","-jar","/rest-api.jar"]