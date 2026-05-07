package com.smartlogix.bff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔥 RUNTIME
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getMessage();

        if (message != null &&
                message.toLowerCase().contains("no encontrado")) {

            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity
                .status(status)
                .body(
                        new ApiResponse<>(
                                status.value(),
                                message,
                                null
                        )
                );
    }

    // 🔥 ERROR GENERAL
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ApiResponse<>(
                                500,
                                "Error interno del servidor",
                                null
                        )
                );
    }
}