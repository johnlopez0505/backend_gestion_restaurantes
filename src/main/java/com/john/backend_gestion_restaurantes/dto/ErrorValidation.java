package com.john.backend_gestion_restaurantes.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorValidation {
    
    private String result;
    private Map<String, String> message;
    private String path;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime dateTime = LocalDateTime.now();

    public static ErrorValidation of (String result, Map<String,String> message, String path) {
        return ErrorValidation.builder()
                .result(result)
                .message(message)
                .path(path)
                .build();
    }
    
}

