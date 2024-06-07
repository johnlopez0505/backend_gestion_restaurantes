package com.john.backend_gestion_restaurantes.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {

    private Long id;
    private Long restauranteId;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;
    
}
