package com.john.backend_gestion_restaurantes.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionDTO {

    private Long id;
    private Long usuarioId;
    private Long restauranteId;
    private Double puntuacion;
    private String comentario;
    private String createdAt;

}
