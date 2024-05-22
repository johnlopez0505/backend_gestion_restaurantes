package com.john.backend_gestion_restaurantes.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

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

    private HttpStatus status;
    private String message;
    private String path;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime dateTime = LocalDateTime.now();

    public static ErrorDetails of (HttpStatus status, String message, String path) {
        return ErrorDetails.builder()
                .status(status)
                .message(message)
                .path(path)
                .build();
    }
    
}
