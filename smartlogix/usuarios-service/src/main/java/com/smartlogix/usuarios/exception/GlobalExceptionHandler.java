package com.smartlogix.usuarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntime(RuntimeException ex) {

        String mensaje = ex.getMessage();
        int status = 400;

        if (mensaje != null && mensaje.toLowerCase().contains("no encontrado")) {
            status = 404;
        }

        return new ResponseEntity<>(
                new ApiResponse(status, mensaje, null),
                status == 404 ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception ex) {
        ex.printStackTrace(); // Log the exception for debugging
        return new ResponseEntity<>(
                new ApiResponse(500, "Error interno del servidor: " + ex.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}