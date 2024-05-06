package com.john.backend_gestion_restaurantes.servicios.ususarios;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.dto.CreateUserRequest;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.modelos.UsuarioRol;
import com.john.backend_gestion_restaurantes.repositorios.RepoUsuarios;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuariosServiceImpl  implements UsuarioService{

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RepoUsuarios repoUsuarios;


    @Override
    public List<Usuario> findAllUsuarios() {
        return repoUsuarios.findAll();
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
        Usuario user =  Usuario.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .imagen(createUserRequest.getAvatar())
                .fullName(createUserRequest.getFullName())
                .roles(roles)
                .build();

        return repoUsuarios.save(user);
    }

    public Usuario createUserWithUserRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UsuarioRol.USUARIO));
    }

    public Usuario createUserWithAdminRole(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UsuarioRol.ADMINISTRADOR));
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
