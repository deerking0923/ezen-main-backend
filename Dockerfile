FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/ezen-main-backend-0.0.1-SNAPSHOT.jar ezen-main-backend.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "ezen-main-backend.jar"]