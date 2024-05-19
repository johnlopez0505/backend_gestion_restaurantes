FROM amazoncorretto:17-alpine-jdk

# Crear el directorio para las imágenes y establecer permisos
RUN mkdir -p src/main/resources/static/imagenes/ && chmod -R 777 src/main/resources/static/imagenes/

COPY target/backend_gestion_restaurantes-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java" , "-jar" , "/app.jar" ]