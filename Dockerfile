FROM amazoncorretto:17-alpine-jdk

COPY target/backend_gestion_restaurantes-0.0.1-SNAPSHOT.jar api.jar

ENTRYPOINT [ "java", "-jar" "/api.jar" ]