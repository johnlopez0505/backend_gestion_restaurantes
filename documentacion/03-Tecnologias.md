\newpage

## Tecnología

### Backend

El backend de la aplicación se desarrolló utilizando diversas tecnologías de la pila de Spring para Java. A continuación,
se presenta una explicación detallada de cada componente, su instalación y configuración.

#### Tecnologías Utilizadas

1. **Spring Boot**: Un framework para desarrollar aplicaciones web y servicios RESTful en Java de manera rápida y sencilla.
2. **Spring MVC**: Un módulo de Spring para el desarrollo de aplicaciones web basado en el patrón Modelo-Vista-Controlador.
3. **Spring Data JPA**: Una abstracción sobre JPA (Java Persistence API) que simplifica el acceso a la base de datos.
4. **Spring Security**: Un módulo de Spring que proporciona autenticación y autorización robustas.
5. **PostgreSQL**: Un sistema de gestión de bases de datos relacional potente y de código abierto.
6. **Fire Base**: Firebase es una plataforma desarrollada por Google que proporciona una serie de herramientas y servicios.

#### Instalación y Configuración

##### 1. Instalación de Spring Boot

Para comenzar con Spring Boot, debes tener instalado Java Development Kit (JDK) y Apache Maven. Una vez que estos
requisitos previos estén en su lugar, puedes crear un nuevo proyecto de Spring Boot.

1. **Crear un Proyecto de Spring Boot**: Puedes crear un proyecto desde el sitio web
   [Spring Initializr](https://start.spring.io/) seleccionando las dependencias necesarias, como Spring Web,
   Spring Data JPA, y Spring Security.

   ![Spring Initializr](https://www.enmilocalfunciona.io/content/images/2023/05/2023-05-17-12_20_45-Spring-Initializr-2.png)

   Alternativamente, puedes crear el proyecto utilizando Maven en la línea de comandos:

    ```bash
    mvn archetype:generate -DgroupId=com.example -DartifactId=restaurant-reservations 
   -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
    ```

2. **Agregar Dependencias**: Edita el archivo `pom.xml` para incluir las dependencias de Spring Boot y otros módulos
   necesarios:

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Otros módulos necesarios -->
    </dependencies>
    ```

#### Configuración de Spring Boot

1. **Configurar el archivo `application.properties`**: Define las propiedades de configuración para la conexión con PostgreSQL:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

2. **Configurar Spring Security**: Define la configuración de seguridad para tu aplicación en una clase `SecurityConfig`:

    ```java
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // Configuración de filtros JWT si es necesario
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Configuración de autenticación
        }
    }
    ```

#### Configuración de Spring Data JPA

1. **Definir Entidades**: Crea clases de entidad que representan las tablas de la base de datos. Por ejemplo, para un
   restaurante:

    ```java
    @Entity
    @Data
    @NoArgsConstructor
    @Buildel
    public class Restaurante {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nombre;
        private String direccion;
        // Otros campos
    }
    ```

2. **Crear Repositorios**: Define interfaces de repositorio para realizar operaciones CRUD sobre las entidades:

    ```java
    public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    }
    ```

#### Configuración de PostgreSQL

**PostgreSQL** es un sistema de gestión de bases de datos relacional y objeto-relacional, conocido por su robustez, 
extensibilidad y soporte para estándares SQL. Es una de las bases de datos más utilizadas en aplicaciones de misión 
crítica y de alto rendimiento.

1. **Instalar PostgreSQL**: Descarga e instala PostgreSQL desde su [sitio oficial](https://www.postgresql.org/download/).

   ![PostgreSQL Download](https://www.postgresql.org/media/img/about/press/elephant.png)

2. **Configurar la Base de Datos**: Una vez instalado, puedes configurar la base de datos para tu aplicación siguiendo 
   estos pasos:

   - **Accede a la Consola de PostgreSQL**: Abre la terminal y accede a la consola de PostgreSQL utilizando el comando 
    `psql`. Es posible que necesites especificar el usuario de PostgreSQL si no estás utilizando el usuario predeterminado.

    ```bash
    sudo -u postgres psql
    ```

   - **Crea una Base de Datos**: Utiliza el comando `CREATE DATABASE` para crear una nueva base de datos. En este caso, 
     crearemos una base de datos llamada `restaurant_db`.

    ```sql
    CREATE DATABASE restaurant_db;
    ```

   - **Crea un Usuario**: Crea un nuevo usuario que tendrá permisos sobre la base de datos. Sustituye `tu_usuario` y 
     `tu_contraseña` por el nombre de usuario y la contraseña que prefieras.

    ```sql
    CREATE USER tu_usuario WITH PASSWORD 'tu_contraseña';
    ```

   - **Asigna Privilegios al Usuario**: Concede todos los privilegios sobre la base de datos recién creada al usuario. 
     Esto incluye permisos para leer, escribir, y modificar datos en la base de datos.

    ```sql
    GRANT ALL PRIVILEGES ON DATABASE restaurant_db TO tu_usuario;
    ```

#### Características Clave de PostgreSQL

- **Fiabilidad y Seguridad**: PostgreSQL es conocido por su durabilidad y consistencia de datos, gracias a 
   características como transacciones ACID, puntos de recuperación y copia de seguridad en caliente.

- **Extensibilidad**: Permite a los usuarios definir sus propios tipos de datos, operadores y funciones. Además, admite 
   extensiones que pueden añadir nuevas funcionalidades al sistema de base de datos.

- **Soporte para JSON**: PostgreSQL soporta tipos de datos JSON y JSONB, lo que facilita el trabajo con datos no 
   estructurados o semi-estructurados.

- **Compatibilidad con SQL Estándar**: PostgreSQL sigue de cerca los estándares SQL, lo que garantiza que el código SQL 
  sea portable y conforme a las especificaciones ANSI SQL.

- **Alta Disponibilidad y Recuperación ante Desastres**: Ofrece soluciones robustas para replicación y failover, 
  asegurando que los datos estén disponibles incluso en caso de fallos del sistema.

#### Configuración Avanzada

- **Ajuste de Parámetros de Configuración**: El archivo `postgresql.conf` permite ajustar una variedad de parámetros 
  que afectan el rendimiento y el comportamiento del servidor PostgreSQL, tales como la memoria compartida, el tamaño 
 del buffer de escritura, y la configuración de los logs.

- **Roles y Seguridad**: PostgreSQL permite una gestión detallada de roles y permisos, lo que es crucial para 
  aplicaciones con múltiples usuarios y niveles de acceso.

#### Integración con Aplicaciones

- **Drivers y Bibliotecas**: Existen numerosos drivers y bibliotecas para integrar PostgreSQL con aplicaciones escritas 
  en diversos lenguajes de programación, como Java (JDBC), Python (psycopg2), Node.js (pg), y muchos más.

- **ORMs (Object-Relational Mappers)**: PostgreSQL es compatible con la mayoría de los ORMs populares, facilitando la 
  interacción entre la base de datos y los modelos de datos en el código de la aplicación. Ejemplos incluyen Hibernate 
  (Java), SQLAlchemy (Python), y Sequelize (Node.js).

Al comprender y aprovechar las capacidades de PostgreSQL, puedes desarrollar aplicaciones robustas, seguras y eficientes, 
maximizando tanto el rendimiento como la mantenibilidad de tus sistemas de bases de datos.


#### Integración de Firebase en el Backend

Firebase es una plataforma desarrollada por Google que proporciona una serie de herramientas y servicios para el
desarrollo de aplicaciones, entre ellos Firebase Storage, que se utiliza para almacenar y servir archivos, como imágenes.
En este proyecto, se utilizará Firebase Storage para almacenar las imágenes de los restaurantes.

#### Pasos para Configurar Firebase

#### 1. Crear un Proyecto en Firebase

1. **Accede a la Consola de Firebase**: Ve a la [Consola de Firebase](https://console.firebase.google.com/) e inicia sesión con tu cuenta de Google.

2. **Crear un Nuevo Proyecto**: Haz clic en "Agregar proyecto" y sigue las instrucciones para crear un nuevo proyecto.

   ![Firebase Create Project](https://firebase.google.com/images/brand-guidelines/logo-logomark.png)

3. **Configurar Firebase Storage**: Una vez que tu proyecto esté creado, navega a la sección "Storage" en el menú de la izquierda y haz clic en "Comenzar" para habilitar Firebase Storage.

   ![Firebase Storage](img/firebase.png)

#### 2. Obtener la Clave de Servicio

1. **Generar Clave de Servicio**: Ve a la configuración del proyecto (icono de engranaje) y selecciona "Cuentas de servicio". Haz clic en "Generar nueva clave privada" para descargar un archivo JSON con las credenciales de tu proyecto.

   ![Firebase Service Account](img/firebase-.png)


#### Configurar Firebase en Spring Boot

1. **Agregar Dependencia de Firebase**: Agrega la dependencia de Firebase al archivo `pom.xml`:

    ```xml
      <dependencies> 
         <dependency>
               <groupId>com.google.firebase</groupId>
               <artifactId>firebase-admin</artifactId>
               <version>6.16.0</version>
          </dependency>
          <dependency>
               <groupId>com.google.cloud</groupId>
               <artifactId>google-cloud-firestore</artifactId>
               <version>3.21.3</version>
          </dependency>
      </dependencies>
    ```

2. **Configurar Firebase**: Inicializa Firebase en tu aplicación utilizando una clave de servicio:

    ```java
    @Configuration
    public class FirebaseConfig {

        @PostConstruct
        public void initialize() {
            try {
                FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream("path/to/serviceAccountKey.json")))
                    .setStorageBucket("your-app.appspot.com")
                    .build();

                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ```

3. **Subir y Obtener Imágenes**: Implementa métodos para subir imágenes a Firebase Storage y obtener sus URLs:

    ```java
    public String uploadImage(MultipartFile file) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(file.getOriginalFilename(), file.getBytes(), file.getContentType());
            return blob.getMediaLink();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    ```


#### Ejecución del Backend en Spring Boot

Spring Boot es un marco de trabajo basado en Spring que simplifica la creación y ejecución de aplicaciones autónomas 
basadas en Java. A continuación, se describen los pasos para configurar, compilar y ejecutar una aplicación backend en 
Spring Boot.


#### 1. Ejecutar la Aplicación

**Desde la Línea de Comandos**:
   - Ejecuta lo siguiente:

    ```bash
    mvn spring-boot:run
    ```

#### 2. Verificar la Ejecución

- Una vez iniciada la aplicación, deberías ver los logs de Spring Boot en la consola, indicando que el servidor embebido
 (por defecto, Tomcat) ha iniciado y está escuchando en el puerto 8080 (a menos que hayas configurado un puerto 
 diferente en `application.properties`).

    ```text
    Tomcat started on port(s): 8080 (http)
    Started Application in 3.45 seconds (JVM running for 5.234)
    ```

- **Acceder a los Endpoints**:
   - Puedes probar los endpoints de tu aplicación utilizando herramientas como Postman o curl. Por ejemplo, para 
   - acceder al endpoint que lista todos los usuarios:

```bash
 curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer tu_token_de_acceso_aqui" \
  -H "Refresh-Token: tu_token_de_refresco_aqui" \
  -H "Content-Type: application/json"
```
Deberías recibir una respuesta en formato JSON con la lista de usuarios o el mensaje de error correspondiente.

#### Guía para hacer Peticiones a la API usando Postman

Esta guía describe cómo hacer peticiones a la API utilizando Postman, incluyendo cómo configurar los encabezados 
necesarios para autenticación con `token` y `refreshToken`.

##### Prerrequisitos

1. Tener Postman instalado.
2. Contar con el `token` de acceso y `refreshToken` proporcionados por tu sistema de autenticación luego de hacer login.

![POST http://localhost:8080/api/auth/login](img/post-login.png)

#### Configuración de Postman

#### 1. Crear una nueva colección

1. Abre Postman.
2. Haz clic en el botón **New** en la esquina superior izquierda.
3. Selecciona **Collection**.
4. Asigna un nombre a tu colección y guarda.

#### 2. Añadir una nueva petición

1. Selecciona la colección que acabas de crear.
2. Haz clic en **Add Request**.
3. Asigna un nombre a tu petición (por ejemplo, `Obtener Usuarios`).

#### 3. Configurar la petición

1. Selecciona el método HTTP `GET`.
2. En el campo **Enter request URL**, introduce `http://localhost:8080/api/usuarios`.

#### 4. Añadir encabezados de autenticación

1. Ve a la pestaña **Headers**.
2. Añade un nuevo encabezado:
    - **Key:** `Authorization`
    - **Value:** `Bearer tu_token_de_acceso_aqui`
      
![Token](img/Token.png)

3. Añade otro encabezado con el `refreshToken`:
    - **Key:** `Refresh-Token`
    - **Value:** `tu_token_de_refresco_aqui`

![RefreshToken](img/refreshToken.png)

4. Añade un encabezado de tipo de contenido:
    - **Key:** `Content-Type`
    - **Value:** `application/json`


#### 5. Enviar la petición

1. Haz clic en el botón **Send**.
2. Revisa la respuesta de la API en la pestaña **Body** de Postman.

![GET http://localhost:8080/api/usuarios](img/get-usuarios.png)

#### Ejemplo de Configuración

##### Headers

| Key            | Value                             |
| -------------- | --------------------------------- |
| Authorization  | Bearer tu_token_de_acceso_aqui    |
| Refresh-Token  | tu_token_de_refresco_aqui         |
| Content-Type   | application/json                  |



### Frontend


#### Guía de React Native con Expo

#### 1. Instalación de Node.js

Para empezar, necesitas tener Node.js instalado en tu máquina. Puedes descargarlo desde la
[página oficial de Node.js](https://nodejs.org/).

#### 2. Verificar la Instalación de Node.js

Para verificar que Node.js se ha instalado correctamente, abre tu terminal y ejecuta los siguientes comandos:

```sh
    node -v
```

#### 3. Instalación de Expo CLI

Expo CLI es una herramienta de línea de comandos que te permite crear y gestionar proyectos de Expo. Para instalar Expo 
CLI, abre tu terminal y ejecuta el siguiente comando:

```bash
    npm install -g expo-cli
```
Creación de un Proyecto
Para crear un nuevo proyecto con Expo, utiliza el siguiente comando:

```bash
  expo init my-new-project
```
Este comando te pedirá que elijas una plantilla para tu proyecto. Selecciona la plantilla que más te convenga (por
ejemplo, "blank" para un proyecto vacío).

Navegar al Directorio del Proyecto
Una vez creado el proyecto, navega al directorio del proyecto:

```bash
  cd my-new-project
```

#### Ejecución del Proyecto
Para iniciar tu proyecto, ejecuta el siguiente comando:

```bash
  expo start
```
Este comando iniciará el servidor de desarrollo y te proporcionará un código QR. Puedes escanear este código QR con la
aplicación Expo Go en tu dispositivo móvil para ver tu aplicación en tiempo real.

![Codigo QR expo](img/qr-expo.png)

Uso de la Aplicación Expo Go
Descarga la aplicación Expo Go desde la App Store o Google Play Store en tu dispositivo móvil. Escanea el código QR
proporcionado por el servidor de desarrollo para ver tu aplicación.

Comandos Útiles
Ejecutar en un Emulador
Si prefieres ejecutar tu aplicación en un emulador en lugar de un dispositivo físico, puedes usar los siguientes comandos:

iOS (requiere macOS y Xcode)
   ```bash
      expo run:ios
   ```


Android
```bash
expo run:android
```


#### Compilar la Aplicación
Para compilar tu aplicación y generar un archivo APK o IPA, puedes usar los siguientes comandos:

Compilar para Android
```bash
eas build --platform android
```

Compilar para iOS
```bash
eas build --platform ios
```
Nota: Para compilar para iOS, necesitas una cuenta de desarrollador de Apple y una máquina con macOS.

\pagebreak
