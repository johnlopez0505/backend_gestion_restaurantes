package com.john.backend_gestion_restaurantes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

   
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String username;

    @NotEmpty(message = "Passwor is required")
    private String password;
    private String verifyPassword;
    private String telefono;
    private boolean enabled;
    private String imagen;
    
    @NotEmpty(message = "Full name is required")
    private String fullName;

}
