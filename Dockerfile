FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
MAINTAINER bootcamp.com
COPY target/*.jar app.jar
RUN apk add curl
ENTRYPOINT ["java","-jar","/app.jar"]