FROM openjdk:8
WORKDIR /usr/app
ENV JARFILE bs_administration-1.0.0-SNAPSHOT.jar
COPY target/JARFILE ./
COPY src/main/resources/application.yml ./
EXPOSE 9100
ENTRYPOINT ["java", "-jar", JARFILE]