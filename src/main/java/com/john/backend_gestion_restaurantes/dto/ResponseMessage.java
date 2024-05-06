package com.john.backend_gestion_restaurantes.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseMessage {

    private HttpStatus status;
    private String message, path;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime dateTime = LocalDateTime.now();

     public static ResponseMessage of (HttpStatus status, String message, String path) {
            return ResponseMessage.builder()
                    .status(status)
                    .message(message)
                    .path(path)
                    .build();
        }
}
