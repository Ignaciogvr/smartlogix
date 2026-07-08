package com.smartlogix.bff.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String JWT_PATTERN = "(?i)Bearer\\s+eyJ[0-9A-Za-z._-]+";

    @ExceptionHandler(BffException.class)
    public ResponseEntity<Map<String, Object>> handleBffException(BffException ex) {
        log.error("[BFF ERROR] BffException: {}", sanitize(ex.getMessage()));
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, Object>> handleExternal(ExternalServiceException ex) {
        log.error("[BFF ERROR] ExternalServiceException: {}", sanitize(ex.getMessage()));
        return buildResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        log.error("[BFF ERROR] ResourceNotFoundException: {}", sanitize(ex.getMessage()));
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        log.warn("[BFF ERROR] Bad request: {}", sanitize(ex.getMessage()));
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((left, right) -> left + ", " + right)
                .orElse("Solicitud invalida");
        log.warn("[BFF ERROR] Validation: {}", sanitize(message));
        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("[BFF ERROR] Exception generica: {}", sanitize(ex.getMessage()), ex);
        return buildResponse("Error al procesar solicitud: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.web.reactive.function.client.WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleWebClientResponse(org.springframework.web.reactive.function.client.WebClientResponseException ex) {
        String responseBody = sanitize(ex.getResponseBodyAsString());
        log.error("[BFF ERROR] WebClientResponseException: {} - Body: {}", ex.getStatusText(), responseBody);
        return buildResponse(
                "Error del microservicio: " + ex.getStatusText() + " - " + responseBody,
                HttpStatus.valueOf(ex.getStatusCode().value())
        );
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(org.springframework.web.server.ResponseStatusException ex) {
        log.warn("[BFF ERROR] ResponseStatusException: {} {}", ex.getStatusCode(), sanitize(ex.getReason()));
        return buildResponse(ex.getReason() != null ? ex.getReason() : ex.getMessage(),
                HttpStatus.valueOf(ex.getStatusCode().value()));
    }


    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {

        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", sanitize(message));

        return new ResponseEntity<>(body, status);
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll(JWT_PATTERN, "Bearer <redacted>");
    }
}
