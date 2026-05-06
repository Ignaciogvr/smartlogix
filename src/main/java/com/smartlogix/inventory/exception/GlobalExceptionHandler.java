package com.smartlogix.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    // 🔥 FIX CONCURRENCIA
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse> handleOptimisticLock() {
        return new ResponseEntity<>(
                new ApiResponse(409, "Conflicto de concurrencia (stock actualizado)", null),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception ex) {
        return new ResponseEntity<>(
                new ApiResponse(500, "Error interno del servidor", null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}