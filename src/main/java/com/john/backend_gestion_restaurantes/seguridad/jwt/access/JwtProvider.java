package com.john.backend_gestion_restaurantes.seguridad.jwt.access;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.seguridad.errorhandling.JwtTokenException;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Log
@Service
public class JwtProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.duration}")
    //private int jwtLifeInDays;
    private int jwtLifeInMinutes;

    private JwtParser jwtParser;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {

        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }


    public String generateToken(Authentication authentication) {

        Usuario user = (Usuario) authentication.getPrincipal();

        return generateToken(user);

    }

    public String generateToken(Usuario user) {
        Date tokenExpirationDateTime =
                Date.from(
                        LocalDateTime
                                .now()
                                //.plusDays(jwtLifeInDays)
                                .plusMinutes(jwtLifeInMinutes)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                );

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(tokenExpirationDateTime)
                .signWith(secretKey)
                .compact();

    }


    public Integer getUserIdFromJwtToken(String token) {
        return Integer.parseInt(
            jwtParser.parseClaimsJws(token).getBody().getSubject()
        );
    }


    public boolean validateToken(String token) {

        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (SignatureException | 
                    MalformedJwtException | 
                    ExpiredJwtException | 
                    UnsupportedJwtException | 
                    IllegalArgumentException ex) {
            log.info("Error con el token: " + ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        }
    }
}
