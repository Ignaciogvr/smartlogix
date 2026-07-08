package com.smartlogix.pedidos.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🔥 VALIDACIONES DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {

        String errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("[ERROR] Validación fallida: {}", errores);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, errores, null));
    }

    // 🔥 ERRORES DE NEGOCIO (INPUT INVALIDO)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(IllegalArgumentException ex) {

        log.error("[ERROR] Error de negocio: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, ex.getMessage(), null));
    }

    // 🔥 ESTADO INVALIDO (IllegalStateException)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleIllegalState(IllegalStateException ex) {

        log.error("[ERROR] Error de estado: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, ex.getMessage(), null));
    }

    // 🔥 RECURSO NO ENCONTRADO (PROFESIONAL)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(EntityNotFoundException ex) {

        log.error("[ERROR] Recurso no encontrado: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, ex.getMessage(), null));
    }

    // 🔥 NULL POINTER (CONTROLADO, PERO RARO)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNull(NullPointerException ex) {

        log.error("[ERROR] NullPointerException: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Error interno: dato nulo - " + ex.getMessage(), null));
    }

    // 🔥 ERROR GENERAL FINAL (CATCH ALL)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception ex) {

        log.error("[ERROR] Error general: {} - StackTrace: {}", ex.getMessage(), getStackTraceAsString(ex));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Error al procesar compra: " + ex.getMessage(), null));
    }

    private String getStackTraceAsString(Exception ex) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}