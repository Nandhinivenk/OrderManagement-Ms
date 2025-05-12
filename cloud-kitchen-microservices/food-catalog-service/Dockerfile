FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/food-catalog-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
