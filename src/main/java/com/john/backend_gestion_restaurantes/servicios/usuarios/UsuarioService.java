package com.john.backend_gestion_restaurantes.servicios.usuarios;

import java.util.List;
import java.util.Optional;

import com.john.backend_gestion_restaurantes.dto.CreateUserRequest;
import com.john.backend_gestion_restaurantes.dto.response.UserResponse;
import com.john.backend_gestion_restaurantes.modelos.Usuario;


public interface UsuarioService {
    List<UserResponse> findAllUsuarios();
    Usuario createUsuario(Usuario usuario);
    Usuario createUserWithUserRole(CreateUserRequest createUserRequest);
    Usuario createUserEntrepreneurRole(CreateUserRequest createUserRequest);
    Usuario createUserWithAdminRole(CreateUserRequest createUserRequest);
    Optional<Usuario> findUsuarioById(Integer id);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> editPassword(Integer userId, String newPassword);
    void deleteUsuario(Integer id);
    boolean passwordMatch(Usuario user, String clearPassword);

}
