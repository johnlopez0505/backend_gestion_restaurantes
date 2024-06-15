package com.john.backend_gestion_restaurantes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionResponse <T>{

    private String result;
    private String message;
    private T calificaciones;

}
    

