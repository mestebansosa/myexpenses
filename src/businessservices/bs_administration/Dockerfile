FROM openjdk:8
WORKDIR /usr/app
ENV JARFILE bs_administration-1.0.0-SNAPSHOT.jar
COPY target/$JARFILE ./
COPY src/main/resources/application-docker.yml ./application.yml
EXPOSE 9110

# ENTRYPOINT has two modes, exec form, does not invoke a command shell, and the shell form
# ENTRYPOINT ["java", "-jar", $JARFILE] this is the exec form, therefore the JARFILE is not got.
ENTRYPOINT java -jar $JARFILE
