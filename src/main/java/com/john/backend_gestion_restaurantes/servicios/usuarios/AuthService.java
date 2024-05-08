package com.john.backend_gestion_restaurantes.servicios.usuarios;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.modelos.UsuarioRol;

@Service
public class AuthService {

    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (Usuario) authentication.getPrincipal();
        }
        return null;
    }

    public Set<UsuarioRol> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(authority -> authority.replaceFirst("ROLE_", ""))
                    .map(UsuarioRol::valueOf)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
