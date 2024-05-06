# Proyecto Sistema Gestion de Restaurantes

## Documentación de la Aplicación de Reservas de Restaurantes

## Descripción del Proyecto

Este proyecto consiste en una aplicación web para realizar reservas en restaurantes. Los usuarios pueden buscar restaurantes, ver detalles, hacer reservas y dejar calificaciones y opiniones.

## Estructura del Proyecto

El proyecto está dividido en varios módulos:

- `src/main/java`: Contiene el código fuente de la aplicación.
- `src/main/resources`: Contiene los recursos estáticos y de configuración.
- `src/test`: Contiene las pruebas unitarias.

![estructuraProyecto](estructuraProyecto.png)

## Tecnologías Utilizadas

### Backend

El backend de la aplicación se desarrollara utilizando Spring Boot, un framework de Java que facilita la creación de aplicaciones web y servicios RESTful. A continuación, se detallan algunas de las características clave de Spring Boot utilizadas en el proyecto:

- **Spring MVC**: Spring MVC es el módulo de Spring utilizado para desarrollar aplicaciones web. Proporciona un modelo de programación basado en controladores para gestionar las solicitudes HTTP y generar respuestas.

- **Spring Data JPA**: Spring Data JPA es una abstracción sobre la API de persistencia de Java (JPA) que simplifica el acceso a la base de datos. Permite definir repositorios que realizan operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre las entidades de la base de datos de forma sencilla.

- **Spring Security**: Spring Security es un módulo de Spring que se utiliza para la autenticación y autorización en aplicaciones web. Permite proteger los endpoints de la API REST y gestionar la seguridad de los usuarios.

- **Spring Boot DevTools**: Spring Boot DevTools es una herramienta que facilita el desarrollo de la aplicación proporcionando funcionalidades como la recarga automática de los cambios en el código y la configuración.

El backend de la aplicación expone una API REST que permite realizar operaciones como búsqueda de restaurantes, gestión de reservas, y obtención de información sobre usuarios y restaurantes.

La comunicación entre el frontend y el backend se realiza a través de esta API REST, siguiendo los principios de arquitectura cliente-servidor.

### Frontend

El frontend de la aplicación se desarrollara utilizando React, una biblioteca de JavaScript popular para construir interfaces de usuario interactivas y dinámicas. A continuación, se detallan algunas de las características clave de React utilizadas en el proyecto:

- **Componentización**: React utiliza componentes reutilizables que permiten dividir la interfaz de usuario en partes más pequeñas y manejables. Cada componente puede tener su propio estado y comportamiento, lo que facilita la construcción y mantenimiento de la aplicación.

- **Virtual DOM**: React utiliza un DOM virtual para optimizar las actualizaciones de la interfaz de usuario. Esto mejora el rendimiento de la aplicación al minimizar el número de manipulaciones del DOM en el navegador.

- **JSX**: JSX es una extensión de JavaScript que permite escribir código HTML dentro de JavaScript. Esto hace que la creación de componentes en React sea más intuitiva y legible.

- **React Hooks**: Los Hooks son una característica de React que permite utilizar el estado y otros aspectos de React en componentes funcionales. Esto simplifica la gestión del estado y el ciclo de vida de los componentes.

- **Librerías y Herramientas**: React cuenta con un amplio ecosistema de librerías y herramientas que facilitan el desarrollo frontend, como React Router para la gestión de rutas, Redux para la gestión del estado global, y Material-UI o Styled Components para el diseño de componentes.

El frontend de la aplicación se comunica con el backend a través de una API REST para realizar operaciones como búsqueda de restaurantes, reserva de mesas, y obtención de calificaciones y opiniones.

## Base de Datos

- MySQL: Sistema de gestión de bases de datos relacional.

El proyecto utiliza una base de datos MySQL con las siguientes tablas:

- `usuarios`: Almacena información sobre los usuarios registrados en la aplicación.
- `restaurantes`: Almacena información sobre los restaurantes disponibles.
- `reservas`: Registra las reservas realizadas por los usuarios.
- `menu`: Almacena información sobre los menus de cada restaurante disponible en la aplicación.
- `calificaciones_opiniones`: Guarda las calificaciones y opiniones dejadas por los usuarios sobre los restaurantes.

### Casos de Uso

#### Usuario sin Registrar

1. **Buscar Restaurante:** Este caso de uso permite a un usuario sin registrar buscar restaurantes en el sistema. Puede buscar por nombre, ubicación u otros criterios.
2. **Ver Detalles del Restaurante:** Permite al usuario sin registrar ver los detalles de un restaurante específico, como su dirección, horario de apertura, menú, etc.
3. **Registrarse:** Este caso de uso permite al usuario sin registrar crear una cuenta en el sistema. Debe proporcionar información como nombre, correo electrónico y contraseña para registrarse.

#### Usuario Registrado

1. **Buscar Restaurantes:** Similar al caso de uso del usuario sin registrar, permite al usuario registrado buscar restaurantes en el sistema.
2. **Ver Detalles del Restaurante:** Permite al usuario registrado ver los detalles de un restaurante específico.
3. **Agregar Restaurante a Favoritos:** Permite al usuario registrado agregar un restaurante a su lista de favoritos para acceder fácilmente en el futuro.
4. **Eliminar Restaurante de Favoritos:** Permite al usuario registrado eliminar un restaurante de su lista de favoritos si ya no desea tenerlo allí.
5. **Realizar Reserva:** Permite al usuario registrado realizar una reserva en un restaurante para una fecha y hora específicas.
6. **Ver Reservas:** Permite al usuario registrado ver sus reservas pasadas y futuras en el sistema.
7. **Editar Perfil:** Permite al usuario registrado editar la información de su perfil, como nombre, dirección, número de teléfono, etc.
8. **Cerrar Sesión:** Permite al usuario registrado cerrar sesión en su cuenta para mantenerla segura.

![casoUso](casoUsos.png)

#### Administrador

1. **Agregar Restaurante:** Permite al administrador agregar un nuevo restaurante al sistema. Debe proporcionar información detallada sobre el restaurante, como nombre, dirección, categoría, etc.
2. **Editar Restaurante:** Permite al administrador editar la información de un restaurante existente, como su dirección, horario de apertura, menú, etc.
3. **Eliminar Restaurante:** Permite al administrador eliminar un restaurante del sistema si ya no está disponible o es necesario por algún motivo.
4. **Ver Usuarios:** Permite al administrador ver la lista de usuarios registrados en el sistema, así como su información básica.
5. **Gestionar Reservas:** Permite al administrador gestionar las reservas realizadas por los usuarios, incluyendo la capacidad de cancelar reservas, cambiar horarios, etc.

## Descripción

1. **Buscar Restaurante**
   - *Descripción:* El usuario puede realizar búsquedas de restaurantes utilizando diferentes criterios como nombre, ubicación, tipo de cocina, etc. El sistema devuelve una lista de resultados que coinciden con los criterios de búsqueda.
   - *Implementación:* Se desarrollará un endpoint en el backend que acepte los parámetros de búsqueda y consulte la base de datos para recuperar los restaurantes que coincidan con esos criterios.

2. **Ver Detalles del Restaurante**
   - *Descripción:* Los usuarios pueden ver los detalles completos de un restaurante específico, incluyendo su dirección, horario de apertura, menú, calificaciones y opiniones de otros usuarios, etc.
   - *Implementación:* Se proporcionará un endpoint en el backend para recuperar la información detallada de un restaurante basándose en su ID único en la base de datos.

3. **Registrarse**
   - *Descripción:* Los usuarios que no tienen una cuenta pueden registrarse en la aplicación proporcionando información básica como nombre, dirección de correo electrónico y contraseña.
   - *Implementación:* Se desarrollará un endpoint en el backend para manejar el proceso de registro, validando la información proporcionada por el usuario y almacenando los detalles en la base de datos.

4. **Autenticación y Autorización**
   - *Descripción:* El sistema debe proporcionar mecanismos de autenticación y autorización para proteger los recursos y endpoints sensibles, asegurando que solo los usuarios autorizados puedan acceder a ellos.
   - *Implementación:* Se utilizará Spring Security para gestionar la autenticación y autorización de los usuarios, utilizando tokens de acceso JWT (JSON Web Tokens) para autenticar las solicitudes.

5. **Gestión de Reservas**
   - *Descripción:* Los usuarios pueden realizar reservas en los restaurantes disponibles seleccionando una fecha, hora y número de comensales. El sistema debe gestionar estas reservas y almacenar la información correspondiente en la base de datos.
   - *Implementación:* Se desarrollarán endpoints en el backend para permitir a los usuarios realizar, cancelar y modificar reservas, así como para proporcionar información sobre las reservas existentes.

6. **Administración de Restaurantes**
   - *Descripción:* Los administradores del sistema pueden agregar, editar y eliminar información sobre los restaurantes, incluyendo su nombre, dirección, horario de apertura, menú, etc.
   - *Implementación:* Se crearán endpoints en el backend para permitir a los administradores realizar estas operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos.

## Configuración

La configuración de la aplicación se encuentra en el archivo `application.properties` en la carpeta `src/main/resources`. Aquí se pueden configurar propiedades como la URL de la base de datos, el puerto del servidor, etc.

## Ejecución

Para ejecutar la aplicación, puedes usar el comando `mvn spring-boot:run` desde la raíz del proyecto. La aplicación estará disponible en `http://localhost:8080`.

## Autor

Este proyecto se desarrollara por `john edinson lopez contreras`
