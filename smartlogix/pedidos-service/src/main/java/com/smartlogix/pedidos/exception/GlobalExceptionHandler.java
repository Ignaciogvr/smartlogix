package com.smartlogix.pedidos.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔥 VALIDACIONES DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {

        String errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, errores, null));
    }

    // 🔥 ERRORES DE NEGOCIO (INPUT INVALIDO)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, ex.getMessage(), null));
    }

    // 🔥 RECURSO NO ENCONTRADO (PROFESIONAL)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(EntityNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, ex.getMessage(), null));
    }

    // 🔥 NULL POINTER (CONTROLADO, PERO RARO)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNull(NullPointerException ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Error interno: dato nulo", null));
    }

    // 🔥 ERROR GENERAL FINAL (CATCH ALL)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Error interno del servidor", null));
    }
}