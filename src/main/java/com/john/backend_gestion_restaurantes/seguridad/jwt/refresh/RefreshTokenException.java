package com.john.sistema_reservas_restaurantes.seguridad.jwt.refresh;

import com.john.sistema_reservas_restaurantes.seguridad.errorhandling.JwtTokenException;

public class RefreshTokenException extends JwtTokenException {

    public RefreshTokenException(String msg) {
        super(msg);
    }

}


