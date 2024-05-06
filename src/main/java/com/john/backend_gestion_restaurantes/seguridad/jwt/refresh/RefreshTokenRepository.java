package com.john.backend_gestion_restaurantes.seguridad.jwt.refresh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.john.backend_gestion_restaurantes.modelos.RefreshToken;
import com.john.backend_gestion_restaurantes.modelos.Usuario;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(Usuario user);

}
