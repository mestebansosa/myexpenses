FROM openjdk:8
WORKDIR /usr/app
COPY target/ds_movements-1.0.0-SNAPSHOT.jar ./
COPY src/main/resources/application.yml ./
EXPOSE 9010
ENTRYPOINT ["java", "-jar", "ds_movements-1.0.0-SNAPSHOT.jar"]