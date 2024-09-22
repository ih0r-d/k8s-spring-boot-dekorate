FROM eclipse-temurin:23-jdk-alpine
COPY target/*.jar k8s-example.jar
CMD java ${JAVA_OPTS} -jar k8s-example.jar