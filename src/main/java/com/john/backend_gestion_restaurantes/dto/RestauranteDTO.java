package com.john.backend_gestion_restaurantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String ciudad;
    private String provincia;
    private String telefono;
    private String imagen;
    private String direccion;
   
}
