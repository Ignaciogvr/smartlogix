package com.smartlogix.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 🔥 ERRORES DE NEGOCIO
    // =========================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        400,
                        ex.getMessage(),
                        null
                ));
    }

    // =========================
    // 🔥 RUNTIME GENERICO
    // =========================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {

        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (message != null && message.toLowerCase().contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity
                .status(status)
                .body(new ApiResponse<>(
                        status.value(),
                        message,
                        null
                ));
    }

    // =========================
    // 🔥 CONCURRENCIA (STOCK / DB)
    // =========================
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleOptimisticLock() {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                        409,
                        "Conflicto de concurrencia (datos actualizados por otro proceso)",
                        null
                ));
    }

    // =========================
    // 🔥 ERROR GLOBAL
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        500,
                        "Error interno del servidor",
                        null
                ));
    }
}