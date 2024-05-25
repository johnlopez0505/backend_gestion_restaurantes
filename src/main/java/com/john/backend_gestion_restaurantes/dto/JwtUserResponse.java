package com.john.backend_gestion_restaurantes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper =  false)
public class JwtUserResponse extends UserResponse {

    private String token;
    private String refreshToken;

    public JwtUserResponse(UserResponse userResponse) {
        id = userResponse.getId();
        username = userResponse.getUsername();
        fullName = userResponse.getFullName();
        imagen = userResponse.getImagen();
        rol = userResponse.getRol();
    }

    public static JwtUserResponse of (Usuario user, String token, String refreshToken, FirebaseStorageService firebaseStorageService) {
        JwtUserResponse result = new JwtUserResponse(UserResponse.fromUser(user, firebaseStorageService));
        result.setToken(token);
        result.setRefreshToken(refreshToken);
        return result;

    }

}
