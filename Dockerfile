FROM ubuntu:20.04

RUN apt-get update -y

RUN apt-get install default-jre -y

WORKDIR /opt/static-file-server

# Build context is target
ADD static-file-server-1.0-SNAPSHOT.jar ./static-file-server.jar

ENTRYPOINT ["java", "-jar", "static-file-server.jar"]