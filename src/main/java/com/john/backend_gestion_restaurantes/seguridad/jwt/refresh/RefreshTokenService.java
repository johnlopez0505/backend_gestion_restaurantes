package com.john.sistema_reservas_restaurantes.seguridad.jwt.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.john.sistema_reservas_restaurantes.modelo.RefreshToken;
import com.john.sistema_reservas_restaurantes.modelo.Usuario;

import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.duration}")
    private int durationInMinutes;


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Usuario user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(durationInMinutes * 60));

        refreshToken = refreshTokenRepository.save(refreshToken);


        return refreshToken;
    }


    public RefreshToken verify(RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            // Token de refresco caducado. Lo eliminamos y lanzamos excepción
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Expired refresh token: " + refreshToken.getToken() + ". Please, login again");
        }

        return refreshToken;


    }

    @Transactional
    public int deleteByUser(Usuario user) {
        return refreshTokenRepository.deleteByUser(user);
    }

}

