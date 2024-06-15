package com.john.backend_gestion_restaurantes.dto.response;

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
    protected String fullName;
    protected String telefono;
    protected boolean enabled;
    protected String rol;
    protected String imagen;


    public static UserResponse fromUser(Usuario user, FirebaseStorageService firebaseStorageService) {

        String rolesString = user.getRoles().toString();
        if (rolesString.startsWith("[") && rolesString.endsWith("]")) {
            rolesString = rolesString.substring(1, rolesString.length() - 1).toLowerCase();
        }

        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .telefono(user.getTelefono())
                .enabled(user.isEnabled())
                .rol(rolesString)
                .imagen(firebaseStorageService.getFileUrl(user.getImagen()))
                .build();  
    }

}
