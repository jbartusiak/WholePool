FROM openjdk:10-jdk-slim
VOLUME /tmp
EXPOSE 8090
ARG JAR_FILE=target/wholepool-rest-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} rest-app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/rest-app.jar"]