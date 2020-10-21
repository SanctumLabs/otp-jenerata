FROM openjdk:11.0.7-jre

ARG JAR_FILE
ADD ${JAR_FILE} service.jar
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "service.jar"]