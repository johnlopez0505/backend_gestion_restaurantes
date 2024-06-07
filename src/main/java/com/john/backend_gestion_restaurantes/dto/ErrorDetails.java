package com.john.backend_gestion_restaurantes.dto;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {

    private String result;
    private String message;
    private String path;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime dateTime = LocalDateTime.now();

    public static ErrorDetails of (String result, String message, String path) {
        return ErrorDetails.builder()
                .result(result)
                .message(message)
                .path(path)
                .build();
    }
    
}
