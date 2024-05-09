package com.john.backend_gestion_restaurantes.controladores;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.john.backend_gestion_restaurantes.dto.ChangePasswordRequest;
import com.john.backend_gestion_restaurantes.dto.CreateUserRequest;
import com.john.backend_gestion_restaurantes.dto.JwtUserResponse;
import com.john.backend_gestion_restaurantes.dto.LoginRequest;
import com.john.backend_gestion_restaurantes.dto.UserResponse;
import com.john.backend_gestion_restaurantes.modelos.RefreshToken;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.seguridad.jwt.access.JwtProvider;
import com.john.backend_gestion_restaurantes.seguridad.jwt.refresh.RefreshTokenException;
import com.john.backend_gestion_restaurantes.seguridad.jwt.refresh.RefreshTokenRequest;
import com.john.backend_gestion_restaurantes.seguridad.jwt.refresh.RefreshTokenService;
import com.john.backend_gestion_restaurantes.servicios.usuarios.UsuarioService;


@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private final AuthenticationManager authManager;

    @Autowired
    private final JwtProvider jwtProvider;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    public UsuarioController(UsuarioService usuarioService, 
                                AuthenticationManager authManager, 
                                JwtProvider jwtProvider, 
                                RefreshTokenService refreshTokenService
                            ) {
        this.usuarioService = usuarioService;
        this.authManager = authManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }


    @GetMapping("/usuarios")
    public ResponseEntity<Object> obtenerTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAllUsuarios();
            Map<String, Object> response = new HashMap<>();
            if (!usuarios.isEmpty()) {
                response.put("result", "ok");
                response.put("usuarios", usuarios.stream().map(
                   usuario -> Map.of("id",usuario.getId(),
                                   "username",usuario.getUsername(),
                                   "rol",usuario.getRoles(),
                                   "fullName",usuario.getFullName(),
                                   "imagen",usuario.getImagen(),
                                   "enable",usuario.isEnabled(),
                                   "token", usuario.getToken()
                                   )
                                )
                            );
                return ResponseEntity.ok(response);
            } else {
                response.put("result", "ok");
                response.put("message", "No se encontro ningun usuario en la base de datos");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("result", "error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al procesar la solicitud: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("get", "/api/usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Object> obtenerUsuarioPorId(@PathVariable Integer id) {
        try {
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);
            Map<String, Object> response = new HashMap<>();
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                response.put("result", "ok");
                response.put("usuarios", Map.of("id",usuario.getId(),
                                "username",usuario.getUsername(),
                                "rol",usuario.getRoles(),
                                "password",usuario.getPassword(),
                                "fullName",usuario.getFullName(),
                                "imagen",usuario.getImagen(),
                                "enable",usuario.isEnabled(),
                                "token", usuario.getToken()
                                )
                            );
                return ResponseEntity.ok(response);
            }else{
                // Manejo del caso en el que no se encuentra el restaurante
                response.put("result", "error");
                response.put("message", "No se encontró un usuario con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
           } catch (Exception e) {
             // Manejo de la excepción
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("result", "error");
             errorResponse.put("details", "Se produjo un error al procesar la solicitud: " + e.getMessage());
             errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
             errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
             errorResponse.put("message", "Se produjo un error al procesar la solicitud: " + e.getMessage());
             errorResponse.put("timestamp", LocalDateTime.now());
             errorResponse.put("get", "/api/usuarios/{id}");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
           }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> createUserWithUserRole(@RequestBody CreateUserRequest createUserRequest) {
        System.out.println("estramos al post ");
        Usuario user = usuarioService.createUserWithUserRole(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }

    @PostMapping("/auth/register/admin")
    public ResponseEntity<UserResponse> createUserWithAdminRole(@RequestBody CreateUserRequest createUserRequest) {
        Usuario user = usuarioService.createUserWithAdminRole(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtUserResponse> login(@RequestBody LoginRequest loginRequest) {
        // Realizamos la autenticación
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

        // Una vez realizada, la guardamos en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Devolvemos una respuesta adecuada
        String token = jwtProvider.generateToken(authentication);
        Usuario user = (Usuario) authentication.getPrincipal();
        user.setToken(token);
        // Eliminamos el token (si existe) antes de crearlo, ya que cada usuario debería tener 
        //solamente un token de refresco simultáneo
        refreshTokenService.deleteByUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.of(user, token, refreshToken.getToken()));

    }


    @PutMapping("/usuarios")
    public ResponseEntity<Object> updateMenu(@RequestHeader Integer id, @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalUsuario.isPresent()) {
                // Manejo del caso en el que no se encuentra el usuario
                response.put("result", "error");
                response.put("message", "No se encontró un usuario con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Usuario existingUsuario = optionalUsuario.get();
            existingUsuario.setFullName(usuario.getFullName());
            existingUsuario.setUsername(usuario.getUsername());;
            existingUsuario.setPassword(usuario.getPassword());;
            existingUsuario.setImagen(usuario.getImagen());
            existingUsuario.setEnabled(usuario.isEnabled());
            existingUsuario.setToken(usuario.getToken());
            System.out.println("usuario existente " + existingUsuario);
            // Guarda el usuario editado en tu base de datos
            Usuario nuevoUsuario = usuarioService.createUsuario(existingUsuario);
            // Devuelve una respuesta exitosa con el usuario editado
            response.put("result", "ok");
            response.put("usuarios", Map.of("id",nuevoUsuario.getId(),
                            "username",nuevoUsuario.getUsername(),
                            "password",nuevoUsuario.getPassword(),
                            "fullName",nuevoUsuario.getFullName(),
                            "imagen",nuevoUsuario.getImagen(),
                            "enable",nuevoUsuario.isEnabled(),
                            "token", nuevoUsuario.getToken()
                            )
                        );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar el usuario : " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("put", "/api/usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/usuarios")
    public ResponseEntity<Object> actualizarRestaurantePatch(@RequestHeader Integer id,  @RequestBody Map<String, Object> updates) {
        try {
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);
            Map<String, Object> response = new HashMap<>();
            if (!optionalUsuario.isPresent()) {
                // Manejo del caso en el que no se encuentra el usuario
                response.put("result", "error");
                response.put("message", "No se encontró un usuario con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Usuario usuarioExistente = optionalUsuario.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            updates.forEach((campo, valor) -> {
                switch (campo) {
                    case "fullName":
                       usuarioExistente.setFullName(valor.toString());
                        break;
                    case "username":
                       usuarioExistente.setUsername(valor.toString());
                        break;
                    case "password":
                       usuarioExistente.setPassword(valor.toString());
                        break;
                    case "imagen":
                       usuarioExistente.setImagen(valor.toString());
                        break;
                    case "enable":
                        usuarioExistente.setEnabled((boolean)valor);;
                        break;
                    case "token":
                        usuarioExistente.setToken(valor.toString());
                        break;
                    default:
                        // Ignora campos desconocidos
                        //response.put(campo, "No se encontró parametro");
                        break;
                }
            });
            Usuario editUsuario = usuarioService.createUsuario(usuarioExistente);
           // Devuelve una respuesta exitosa con el usuario editado
           response.put("result", "ok");
           response.put("usuarios", Map.of("id",editUsuario.getId(),
                           "username",editUsuario.getUsername(),
                           "password",editUsuario.getPassword(),
                           "fullName",editUsuario.getFullName(),
                           "imagen",editUsuario.getImagen(),
                           "enable",editUsuario.isEnabled(),
                           "token", editUsuario.getToken()
                           )
                       );
           return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de la excepción
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            errorResponse.put("message", "Se produjo un error al actualizar el usuario: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("path", "/api/usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Integer id) {
        try {
            // Buscar el usuario por su ID
            Optional<Usuario> usuarioOptional = usuarioService.findUsuarioById(id);
            Map<String, Object> response = new HashMap<>();
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                // Si se encuentra el usuario, eliminarlo de la base de datos
                usuarioService.deleteUsuario(usuario.getId().intValue());
                // Devolver una respuesta indicando que el usuario fue eliminado correctamente
                response.put("result", "ok");
                response.put("message", "El usuario se eliminó correctamente");
                return ResponseEntity.ok(response);
            } else {
                // Si no se encuentra el usuario con el ID proporcionado, 
                // devolver una respuesta indicando que no se encontró el usuario
                response.put("result", "error");
                response.put("message", "No se encontró un Usuario con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
             // Manejo de la excepción
             Map<String, Object> errorResponse = new HashMap<>();
             errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
             errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
             errorResponse.put("message", "Se produjo un error al eliminar el usuario: " + e.getMessage());
             errorResponse.put("timestamp", LocalDateTime.now());
             errorResponse.put("delete", "/api/usuarios/{id}");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

   

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verify)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtProvider.generateToken(user);
                    refreshTokenService.deleteByUser(user);
                    RefreshToken refreshToken2 = refreshTokenService.createRefreshToken(user);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(JwtUserResponse.builder()
                                    .token(token)
                                    .refreshToken(refreshToken2.getToken())
                                    .build());
                })
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

    }



    @PutMapping("/usuarios/changePassword")
    public ResponseEntity<UserResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                       @AuthenticationPrincipal Usuario loggedUser) {

        // Este código es mejorable.
        // La validación de la contraseña nueva se puede hacer con un validador.
        // La gestión de errores se puede hacer con excepciones propias
        try {
            if (usuarioService.passwordMatch(loggedUser, changePasswordRequest.getOldPassword())) {
                Optional<Usuario> modified = usuarioService.editPassword(loggedUser.getId().intValue(), changePasswordRequest.getNewPassword());
                if (modified.isPresent())
                    return ResponseEntity.ok(UserResponse.fromUser(modified.get()));
            } else {
                // Lo ideal es que esto se gestionara de forma centralizada
                // Se puede ver cómo hacerlo en la formación sobre Validación con Spring Boot
                // y la formación sobre Gestión de Errores con Spring Boot
                throw new RuntimeException();
            }
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password Data Error");
        }

        return null;
    }


}
