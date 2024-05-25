package com.john.backend_gestion_restaurantes.dto;

import com.john.backend_gestion_restaurantes.modelos.Usuario;
import com.john.backend_gestion_restaurantes.servicios.imagenes.FirebaseStorageService;

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
    


    public static UserResponse fromUser(Usuario user, FirebaseStorageService firebaseStorageService) {

        String rolesString = user.getRoles().toString();
        if (rolesString.startsWith("[") && rolesString.endsWith("]")) {
            rolesString = rolesString.substring(1, rolesString.length() - 1).toLowerCase();
        }

        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .imagen(firebaseStorageService.getFileUrl(user.getImagen()))
                .fullName(user.getFullName())
                .rol(rolesString)
                .build();  
    }

}
