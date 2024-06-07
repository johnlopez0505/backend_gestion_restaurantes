package com.john.backend_gestion_restaurantes.configuracion;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.john.backend_gestion_restaurantes.modelos.Usuario;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // return Optional.ofNullable(authentication)
        //         .filter(auth -> auth.getPrincipal() instanceof Usuario)
        //         .map(auth -> (Usuario) auth.getPrincipal())
        //         .map(Usuario::getId)
        //         .map(Object::toString);

        return Optional.ofNullable(authentication)
            .filter(auth -> auth.getPrincipal() instanceof Usuario)
            .map(auth -> (Usuario) auth.getPrincipal())
            .map(usuario -> String.valueOf(usuario.getId())); 

    }
}

