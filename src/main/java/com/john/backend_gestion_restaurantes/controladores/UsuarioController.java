package com.john.backend_gestion_restaurantes.controladores;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.john.backend_gestion_restaurantes.dto.ChangePasswordRequest;
import com.john.backend_gestion_restaurantes.dto.CreateUserRequest;
import com.john.backend_gestion_restaurantes.dto.JwtUserResponse;
import com.john.backend_gestion_restaurantes.dto.LoginRequest;
import com.john.backend_gestion_restaurantes.dto.response.UserResponse;
import com.john.backend_gestion_restaurantes.dto.response.UsuarioResponse;
import com.john.backend_gestion_restaurantes.modelos.RefreshToken;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.seguridad.jwt.access.JwtProvider;
import com.john.backend_gestion_restaurantes.seguridad.jwt.refresh.RefreshTokenException;
import com.john.backend_gestion_restaurantes.seguridad.jwt.refresh.RefreshTokenRequest;
import com.john.backend_gestion_restaurantes.seguridad.jwt.refresh.RefreshTokenService;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;
import com.john.backend_gestion_restaurantes.servicios.usuarios.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


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

     @Autowired
    private FirebaseStorageService firebaseStorageService;


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

    @GetMapping("/usuarios/{id}")
    public ResponseEntity< UsuarioResponse<UserResponse>> obtenerUsuarioPorId(@PathVariable Integer id) {
        try {
            Optional<Usuario> usuario = usuarioService.findUsuarioById(id);
            if (!usuario.isPresent()) {
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                    "error", "No se encontró un usuario con el ID proporcionado", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                "ok", "Usuario listado con éxito", UserResponse.fromUser(usuario.get(), firebaseStorageService));
                return ResponseEntity.ok(response);
        } catch (Exception e) {
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "error", "Ocurrió un error al listar el Usuario", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    @PostMapping("/auth/register")
    public ResponseEntity<UsuarioResponse<UserResponse> > createUserWithUserRole(@Valid @RequestBody CreateUserRequest createUserRequest) {
        Usuario user = usuarioService.createUserWithUserRole(createUserRequest);
        UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario registrado con éxito", (UserResponse.fromUser(user, firebaseStorageService)));
            return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register/empresario")
    public ResponseEntity<UsuarioResponse<UserResponse>> createUserWithEntrepreneurRole(@Valid @RequestBody CreateUserRequest createUserRequest) {
        Usuario user = usuarioService.createUserEntrepreneurRole(createUserRequest);
        UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario registrado con éxito", (UserResponse.fromUser(user, firebaseStorageService)));
            return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register/admin")
    public ResponseEntity<UsuarioResponse<UserResponse>> createUserWithAdminRole(@Valid @RequestBody CreateUserRequest createUserRequest) {
        Usuario user = usuarioService.createUserWithAdminRole(createUserRequest);
        UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario registrado con éxito", UserResponse.fromUser(user, firebaseStorageService));
            return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UsuarioResponse<JwtUserResponse>> login(@RequestBody LoginRequest loginRequest) {
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
        UsuarioResponse<JwtUserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario listado con éxito", JwtUserResponse.of(user, token, refreshToken.getToken(), firebaseStorageService));
            return ResponseEntity.ok(response);
    }


    @PutMapping("/usuarios/edit/{id}")
    public ResponseEntity<UsuarioResponse<UserResponse>> updateMenu(@PathVariable Integer id, @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);
            if (!optionalUsuario.isPresent()) {
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                "error", "No se encontró un usuario con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Usuario existingUsuario = optionalUsuario.get();
            existingUsuario.setFullName(usuario.getFullName());
            existingUsuario.setTelefono(usuario.getTelefono());
            existingUsuario.setEnabled(usuario.isEnabled());

             // Guardar la nueva imagen si existe
             if (usuario.getImagen() != null && !usuario.getImagen().isEmpty()) {
                String newFileName = 
                       firebaseStorageService.updateFile(existingUsuario.getImagen(), usuario.getImagen());
                existingUsuario.setImagen(newFileName);
            }

            // Guarda el usuario editado en tu base de datos
            Usuario nuevoUsuario = usuarioService.createUsuario(existingUsuario);
            // Devuelve una respuesta exitosa con el usuario editado
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario editado con éxito", UserResponse.fromUser(nuevoUsuario, firebaseStorageService));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "error", "Ocurrió un error al editar el Usuario: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/usuarios/edit/{id}")
    public  ResponseEntity<UsuarioResponse<UserResponse>>  actualizarRestaurantePatch(@PathVariable Integer id,  @RequestBody Map<String, Object> updates) {
        try {
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);
            if (!optionalUsuario.isPresent()) {
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                "error", "No se encontró un usuario con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            Usuario usuarioExistente = optionalUsuario.get();
            // Actualizar cada campo proporcionado en el objeto JSON de actualizaciones
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();
                switch (campo) {
                    case "fullName":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            usuarioExistente.setFullName(valor.toString());
                        }
                        break;
                    case "imagen":
                        // Guardar la nueva imagen si existe
                        if (valor.toString() != null && ! valor.toString().isEmpty()) {
                            String newFileName = firebaseStorageService.updateFile(
                                usuarioExistente.getImagen(), valor.toString());
                            usuarioExistente.setImagen(newFileName);
                        }
                        break;
                    case "enable":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            usuarioExistente.setEnabled((boolean)valor);
                        }
                        break;
                    case "telefono":
                        if (valor.toString() != null || ! valor.toString().isEmpty()) {
                            usuarioExistente.setTelefono(valor.toString());
                        }
                        break;
                    default:
                        UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                        "error", "No se encontró parametro: " + campo,  null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            };
            Usuario editUsuario = usuarioService.createUsuario(usuarioExistente);
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario editado con éxito", UserResponse.fromUser(editUsuario, firebaseStorageService));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "error", "Ocurrió un error al editar el Usuario: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/usuarios/delete/{id}")
    public ResponseEntity< UsuarioResponse<UserResponse>> deleteUsuario(@PathVariable Integer id) {
        try {
            // Buscar el usuario por su ID
            Optional<Usuario> optionalUsuario = usuarioService.findUsuarioById(id);
            if (!optionalUsuario.isPresent()) {
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                "error", "No se encontró un usuario con el ID proporcionado", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Usuario usuario = optionalUsuario.get();
            // Eliminar el usuario de la base de datos
            usuarioService.deleteUsuario(usuario.getId().intValue());
            // Devuelve una respuesta exitosa
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Usuario eliminado con éxito",null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "error", "Ocurrió un error al eliminar el Usuario: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
                    user.setToken(token);
                    refreshTokenService.deleteByUser(user);
                    RefreshToken refreshToken2 = refreshTokenService.createRefreshToken(user);
                    // return ResponseEntity.status(HttpStatus.CREATED)
                    //         .body(JwtUserResponse.builder()
                    //                 .token(token)
                    //                 .refreshToken(refreshToken2.getToken())
                    //                 .build());
                    JwtUserResponse jwtUserResponse = JwtUserResponse.of(user, token, refreshToken2.getToken(), firebaseStorageService);
                    return ResponseEntity.status(HttpStatus.CREATED).body(jwtUserResponse);
                })
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

    }



    @PutMapping("/usuarios/changePassword")
    public ResponseEntity<UsuarioResponse<UserResponse>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                       @AuthenticationPrincipal Usuario loggedUser,
                                                       HttpServletRequest request) {
        try {
            if (!usuarioService.passwordMatch(loggedUser, changePasswordRequest.getOldPassword())) {
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                    "error", " La contraseña antigua no coincide.", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Optional<Usuario> modified = usuarioService.editPassword(loggedUser.getId().intValue(), 
                                        changePasswordRequest.getNewPassword());
            
            if (!modified.isPresent()) {
                UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
                "error", "No se pudo cambiar la contraseña", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            // Devuelve una respuesta exitosa
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "ok", "Contraseña editada con éxito",null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            UsuarioResponse<UserResponse> response = new UsuarioResponse<>(
            "error", "Ocurrió un error al editar la contraseña: " +e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}
