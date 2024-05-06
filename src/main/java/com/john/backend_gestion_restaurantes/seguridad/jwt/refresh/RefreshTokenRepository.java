package com.john.sistema_reservas_restaurantes.seguridad.jwt.refresh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.john.sistema_reservas_restaurantes.modelo.RefreshToken;
import com.john.sistema_reservas_restaurantes.modelo.Usuario;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(Usuario user);

}
