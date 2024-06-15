\newpage

## Implementación

### Explicación del Código Relevante

#### GenerateToken()

En el siguiente fragmento de código en Java se muestra la función `generateToken`, la cual es responsable de generar un 
token JWT para autenticación y autorización de usuarios en la aplicación:

```java
 public String generateToken(Usuario user) {
        Date tokenExpirationDateTime =
            Date.from(
                Instant.now().plus(Duration.ofDays(30))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
            );

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(tokenExpirationDateTime)
                .signWith(secretKey)
                .compact();
}
```
Funcionamiento:

- `Fecha de Expiración`: La función calcula la fecha de expiración del token sumando 30 días a la fecha y hora actuales.
- `Construcción del Token JWT`: Utiliza la biblioteca JJWT para construir el token JWT con los siguientes parámetros:
   - `Header (typ)`: Tipo del token, en este caso "JWT".
   - `Subject`: Identifica al usuario dentro del token, utilizando el ID del usuario convertido a string.
- `Issued At (iat)`: Fecha y hora en que se emitió el token.
- `Expiration (exp)`: Fecha y hora de expiración del token.
- `Firma del Token`: Utiliza una clave secreta (secretKey) para firmar el token, asegurando su autenticidad y seguridad. 
Esta función es crucial para generar tokens seguros que permitan la autenticación de usuarios en la aplicación de manera
eficiente y segura, utilizando estándares de seguridad modernos como JWT.

\newpage

#### ExceptionControllerAdvice()
##### Controlador de Excepciones para Autenticación

El siguiente fragmento de código Java muestra un controlador de excepciones (`ExceptionControllerAdvice`) utilizado en 
una aplicación Spring Boot para manejar excepciones relacionadas con la autenticación:

```java
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer")
                .body(ErrorDetails.of(
                        "error",
                        "Error de autenticación: " + ex.getMessage(),
                        request.getRequestURI()));

    }
}

```

**`@RestControllerAdvice`:** Esta anotación marca esta clase como un consejo global para manejar excepciones en 
controladores REST dentro de la aplicación Spring Boot.

**`@ExceptionHandler({ AuthenticationException.class })`:** Este método es invocado cuando se produce una excepción del 
tipo `AuthenticationException`, indicando un error de autenticación.

**Respuesta de Error (`ResponseEntity`):**

- **Estado HTTP (`HttpStatus.UNAUTHORIZED`):** Devuelve un código de estado HTTP 401 (Unauthorized) indicando que la 
  solicitud no tiene autorización adecuada.

- **Cabecera `WWW-Authenticate`:** Agrega una cabecera `WWW-Authenticate` con el valor "Bearer", indicando que se espera
   un token de autenticación en formato Bearer en las solicitudes futuras.

- **Cuerpo de la respuesta (`body`):** Utiliza la clase `ErrorDetails.of` para crear un objeto de detalles de error que 
   contiene:
    - Tipo de error ("error").
    - Mensaje de error específico ("Error de autenticación: " + ex.getMessage()), que concatena el mensaje de la 
       excepción `AuthenticationException`.
    - URI de la solicitud actual (`request.getRequestURI()`), que proporciona la ruta URI que originó la excepción.

Este controlador de excepciones asegura que las excepciones de autenticación sean manejadas de manera adecuada, 
devolviendo respuestas HTTP claras y proporcionando información útil para diagnosticar y solucionar problemas 
relacionados con la autenticación en la aplicación Spring Boot.


#### CustomUserDetailsService()

Este servicio `CustomUserDetailsService` es crucial en la configuración de seguridad de Spring, asegurando que los
detalles del usuario sean cargados correctamente durante el proceso de autenticación y autorización en la aplicación.

```java
    @Service("userDetailsService")
    @RequiredArgsConstructor
    public class CustomUserDetailsService implements UserDetailsService {
    
        @Autowired
        private final UsuarioService userService;
    
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return userService.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No se encontro usuario con el username: " +  username));
        }
    }
```

**`@Service("userDetailsService")`:** Marca la clase `CustomUserDetailsService` como un servicio gestionado por Spring 
con el nombre "userDetailsService". Este servicio implementa la interfaz `UserDetailsService`, que es esencial para 
cargar detalles de usuario durante el proceso de autenticación.

**`@RequiredArgsConstructor`:** Lombok genera un constructor para la clase que inicializa automáticamente el campo 
`final UsuarioService userService`. Este servicio se utiliza para interactuar con la capa de persistencia y recuperar 
los detalles del usuario según el nombre de usuario proporcionado.

**`loadUserByUsername(String username)`:** Método implementado de la interfaz 
UserDetailsService que carga los detalles del usuario basado en el nombre de usuario proporcionado. 
Utiliza el servicio `UsuarioService` para buscar un usuario por su nombre de usuario. Si no se encuentra 
ningún usuario, se lanza una excepción `UsernameNotFoundException` con un mensaje adecuado.

\newpage

#### UsuarioController()

Este controlador `UsuarioController` proporciona endpoints RESTful para manejar operaciones relacionadas con los
usuarios, como obtener una lista de usuarios almacenados en la base de datos. Utiliza inyección de dependencias para
acceder a los servicios y componentes necesarios para ejecutar estas operaciones de manera eficiente y segura.

```java

    @RestController
    @RequestMapping("/api")
    public class UsuarioController {
    
        @Autowired
        private UsuarioService usuarioService;
    
        @GetMapping("/usuarios")
        public ResponseEntity<UsuarioResponse<List<UserResponse>>> obtenerTodosLosUsuarios() {
            try {
                List<UserResponse> usuarios = usuarioService.findAllUsuarios();
                if (usuarios == null || usuarios.isEmpty()) {
                    UsuarioResponse<List<UserResponse>>
                            response = new UsuarioResponse<>(
                            "error", "No hay usuarios almacenados en la base de datos", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
                UsuarioResponse<List<UserResponse>> response = new UsuarioResponse<>(
                        "ok", "Usuarios listados con éxito", usuarios);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                UsuarioResponse<List<UserResponse>> response = new UsuarioResponse<>(
                        "error", "Ocurrió un error al listar los usuarios: " + e.getMessage(), null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    }
```

**`@RestController`:** Esta anotación marca la clase `UsuarioController` como un controlador de Spring que maneja las 
solicitudes REST.

**`@RequestMapping("/api")`:** Especifica que todas las solicitudes HTTP manejadas por este controlador deben comenzar 
con `/api` en su ruta URL.

\newpage

**Inyección de Dependencias:**
- **`@Autowired private UsuarioService usuarioService;`**: Inyecta una instancia del servicio `UsuarioService`, que se 
  utiliza para interactuar con los datos de los usuarios en la capa de servicio.

**`@GetMapping("/usuarios")`:** Este método maneja las solicitudes GET dirigidas a `/api/usuarios`. Retorna una 
respuesta HTTP que contiene una lista de usuarios en formato JSON.

**Funcionalidad del Método:**
- Intenta obtener todos los usuarios utilizando `usuarioService.findAllUsuarios()`.
- Si no se encuentran usuarios (`usuarios == null || usuarios.isEmpty()`), devuelve una respuesta con estado HTTP 404 
 (Not Found) y un mensaje indicando que no hay usuarios almacenados.
- Si se encuentran usuarios, devuelve una respuesta con estado HTTP 200 (OK) y la lista de usuarios junto con un mensaje
  de éxito.
- Si ocurre alguna excepción durante el procesamiento, captura la excepción, devuelve una respuesta con estado HTTP 500 
 (Internal Server Error) y un mensaje indicando el error ocurrido.


