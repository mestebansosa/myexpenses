FROM openjdk:8
WORKDIR /usr/app
COPY target/bs_movements-1.0.0-SNAPSHOT.jar ./
COPY src/main/resources/application.yml ./
EXPOSE 9110
ENTRYPOINT ["java", "-jar", "bs_movements-1.0.0-SNAPSHOT.jar"]