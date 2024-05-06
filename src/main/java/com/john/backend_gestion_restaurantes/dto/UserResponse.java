package com.john.backend_gestion_restaurantes.dto;

import com.john.backend_gestion_restaurantes.modelos.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class UserResponse {

    protected String id;
    protected String username, avatar, fullName;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    // protected LocalDateTime createdAt;


    public static UserResponse fromUser(Usuario user) {

        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .avatar(user.getImagen())
                .fullName(user.getFullName())
                .build();
    }

}
