FROM openjdk:10-jdk-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/wholepool-web-0.0.1-SNAPSHOT.war
ADD ${JAR_FILE} web-app.war
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/web-app.war"]