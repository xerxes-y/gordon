FROM openjdk:17.0.2-jdk-slim
EXPOSE 8080
VOLUME /tmp
COPY target/*.jar app.jar
RUN sh -c 'touch app.jar'
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongodb:27017/kitchen", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/app.jar"]
HEALTHCHECK --interval=1m --timeout=3s CMD wget -q -T 3 -s http://localhost:8080

