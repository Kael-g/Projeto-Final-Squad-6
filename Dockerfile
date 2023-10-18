FROM openjdk:11-jre
EXPOSE 8080
COPY target/ApiReceitas-0.0.1-SNAPSHOT.jar ApiReceitas-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ApiReceitas-0.0.1-SNAPSHOT.jar"]