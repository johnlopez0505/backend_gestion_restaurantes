package com.john.backend_gestion_restaurantes.seguridad.jwt.refresh;

import com.john.backend_gestion_restaurantes.seguridad.errorhandling.JwtTokenException;

public class RefreshTokenException extends JwtTokenException {

    public RefreshTokenException(String msg) {
        super(msg);
    }

}


