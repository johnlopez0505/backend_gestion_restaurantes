package com.john.backend_gestion_restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    
    private Long id;
    private String username;
    private String imagen;
    private String telefono;
    private String fullName;
    private String rol;
    private String refreshToken;
    private String token;
}
