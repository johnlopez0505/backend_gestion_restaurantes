package com.john.backend_gestion_restaurantes.seguridad.errorhandling;

import com.john.backend_gestion_restaurantes.dto.ErrorDetails;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@RestControllerAdvice
public class TokenControllerAdvice  {

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer")
                .body(ErrorDetails.of(
                    HttpStatus.UNAUTHORIZED, 
                    "Error de autenticación: " + ex.getMessage(), 
                    request.getRequestURI()));

    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDetails.of(HttpStatus.FORBIDDEN, 
                    "Acceso denegado: " + ex.getMessage(), 
                    request.getRequestURI()));

    }


    @ExceptionHandler({JwtTokenException.class})
    public ResponseEntity<?> handleTokenException(JwtTokenException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDetails.of(HttpStatus.FORBIDDEN, 
                    "Error al procesar el token JWT: " + ex.getMessage(), 
                    request.getRequestURI()));
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<?> handleUserNotExistsException(UsernameNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorDetails.of(
                        HttpStatus.UNAUTHORIZED,
                        "El usuario no fue encontrado en el sistema.",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorDetails.of(
                        HttpStatus.CONFLICT,
                        "El correo electrónico ya está en uso",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( ErrorDetails.of(HttpStatus.CONFLICT,
                "El archivo es muy grande ",
                request.getRequestURI()));
    }
}
