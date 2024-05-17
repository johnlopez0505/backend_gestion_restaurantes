FROM amazoncorretto:17-alpine-jdk

# Crear el directorio para las imágenes
RUN mkdir -p /app/imagenes

COPY target/backend_gestion_restaurantes-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java" , "-jar" , "/app.jar" ]