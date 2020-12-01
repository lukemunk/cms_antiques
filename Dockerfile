FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=target/cms_antiques-0.0.1-SNAPSHOT.jar
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]