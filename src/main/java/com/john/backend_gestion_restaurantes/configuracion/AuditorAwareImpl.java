package com.john.backend_gestion_restaurantes.configuracion;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.john.backend_gestion_restaurantes.modelos.Usuario;

import java.util.Optional;
/**
 * Implementación de {@link AuditorAware} que proporciona el identificador del auditor actual.
 *
 * <p>Esta implementación utiliza el contexto de seguridad de Spring para obtener la autenticación
 * actual y extraer el identificador del usuario autenticado.</p>
 *
 * <p>Si la autenticación actual contiene un principal que es una instancia de {@link Usuario},
 * se devuelve el identificador de ese usuario como un {@link String}.</p>
 *
 * <p>De lo contrario, se devuelve un {@link Optional} vacío.</p>
 * <p>Para usar esta clase, asegúrate de que el contexto de seguridad esté correctamente configurado
 * y que las instancias de autenticación contengan un principal de tipo {@link Usuario}.</p>
 *
 * @see AuditorAware
 * @see SecurityContextHolder
 * @see Authentication
 * @see Usuario
 * @author john-lopez
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Obtiene el auditor actual.
     *
     * <p>Este método obtiene la autenticación actual del contexto de seguridad y, si el principal
     * es una instancia de {@link Usuario}, devuelve el identificador del usuario autenticado.</p>
     *
     * @return un {@link Optional} que contiene el identificador del usuario autenticado, o un
     * {@link Optional} vacío si no hay autenticación o si el principal no es una instancia de
     * {@link Usuario}.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(authentication)
            .filter(auth -> auth.getPrincipal() instanceof Usuario)
            .map(auth -> (Usuario) auth.getPrincipal())
            .map(usuario -> String.valueOf(usuario.getId())); 

    }
}

