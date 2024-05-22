package com.john.backend_gestion_restaurantes.dto;

import com.john.backend_gestion_restaurantes.modelos.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor 
@AllArgsConstructor
@SuperBuilder
public class UserResponse {

    protected String id;
    protected String username;
    protected String imagen;
    protected String fullName;
    protected String rol;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    // protected LocalDateTime createdAt;


    public static UserResponse fromUser(Usuario user, String baseUrl) {

        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .imagen(user.getImagen() != null ? (baseUrl + user.getImagen()) : "sin imagen")
                .fullName(user.getFullName())
                .rol(user.getRoles().toString())
                .build();  
    }

}
