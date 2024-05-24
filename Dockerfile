# Stage 1: Build the project
FROM amazoncorretto:17-alpine-jdk AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y el código fuente
COPY pom.xml .
COPY src src

# Copiar el Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Dar permisos de ejecución al Maven wrapper
RUN chmod +x ./mvnw

# Ejecutar Maven para construir el proyecto, omitiendo las pruebas
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:17-alpine-jdk

# Copiar el JAR construido desde la etapa de construcción
COPY --from=build /app/target/backend_gestion_restaurantes-0.0.1-SNAPSHOT.jar /app/app.jar


# Establecer el punto de entrada
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Exponer el puerto 8080
EXPOSE 8080
