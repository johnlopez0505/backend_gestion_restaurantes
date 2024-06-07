package com.john.backend_gestion_restaurantes.servicios.usuarios;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.john.backend_gestion_restaurantes.dto.CreateUserRequest;
import com.john.backend_gestion_restaurantes.dto.response.UserResponse;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.modelos.UsuarioRol;
import com.john.backend_gestion_restaurantes.repositorios.RepoUsuarios;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl  implements UsuarioService{

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RepoUsuarios repoUsuarios;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private  String newFileName;


    @Override
    public List<UserResponse> findAllUsuarios() {
        List<Usuario> usuarios = repoUsuarios.findAll();
         return usuarios.stream()
        .map(usuario -> UserResponse.fromUser(usuario, firebaseStorageService))
        .collect(Collectors.toList());
    }

    @Override
    public Usuario  createUsuario(Usuario usuario) {
        return repoUsuarios.save(usuario);
    }


    @Override
    public Optional<Usuario> findUsuarioById(Integer id) {
        return repoUsuarios.findById(id);
    }

    @Override
    public void deleteUsuario(Integer id) {
        repoUsuarios.deleteById(id);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repoUsuarios.findFirstByUsername(username);
    }


    public Usuario createUser(CreateUserRequest createUserRequest, EnumSet<UsuarioRol> roles) {
        try {
            if (createUserRequest.getImagen() != null && !createUserRequest.getImagen().isEmpty()) {
                newFileName = firebaseStorageService.uploadBase64Image(createUserRequest.getImagen());
            }else{
                newFileName = "b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg";
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al guardar la imagen");
        }
        Usuario user =  Usuario.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .imagen(newFileName)
                .telefono(createUserRequest.getTelefono())
                .fullName(createUserRequest.getFullName())
                .roles(roles)
                .build();

        return repoUsuarios.save(user);
    }

    public Usuario createUserWithUserRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UsuarioRol.USUARIO));
    }

    @Override
    public Usuario createUserEntrepreneurRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UsuarioRol.EMPRESARIO));
    }


    public Usuario createUserWithAdminRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UsuarioRol.ADMIN));
    }

    public boolean passwordMatch(Usuario user, String clearPassword) {
        return passwordEncoder.matches(clearPassword, user.getPassword());
    }


    public Optional<Usuario> editPassword(Integer userId, String newPassword) {
        // Aquí no se realizan comprobaciones de seguridad. Tan solo se modifica
        return repoUsuarios.findById(userId)
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(newPassword));
                    return repoUsuarios.save(u);
                }).or(() -> Optional.empty());
    }

    

}
