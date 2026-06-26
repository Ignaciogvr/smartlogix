package com.smartlogix.bff.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    // =========================
    // ERROR INFO
    // =========================

    private int status;

    private String error;

    private String message;

    private String service;

    private String path;

    private String traceId;

    private LocalDateTime timestamp;

    // =========================
    // VALIDATION ERRORS
    // =========================

    private List<FieldErrorDetail> errors;

    // =========================
    // CONSTRUCTORES
    // =========================

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(
            int status,
            String error,
            String message
    ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(
            int status,
            String error,
            String message,
            String service,
            String path,
            String traceId
    ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.service = service;
        this.path = path;
        this.traceId = traceId;
        this.timestamp = LocalDateTime.now();
    }

    // =========================
    // FACTORY METHODS
    // =========================

    public static ErrorResponse badRequest(
            String message
    ) {

        return new ErrorResponse(
                400,
                "BAD_REQUEST",
                message
        );
    }

    public static ErrorResponse unauthorized(
            String message
    ) {

        return new ErrorResponse(
                401,
                "UNAUTHORIZED",
                message
        );
    }

    public static ErrorResponse forbidden(
            String message
    ) {

        return new ErrorResponse(
                403,
                "FORBIDDEN",
                message
        );
    }

    public static ErrorResponse notFound(
            String message
    ) {

        return new ErrorResponse(
                404,
                "NOT_FOUND",
                message
        );
    }

    public static ErrorResponse internal(
            String message
    ) {

        return new ErrorResponse(
                500,
                "INTERNAL_SERVER_ERROR",
                message
        );
    }

    public static ErrorResponse externalService(
            String message
    ) {

        return new ErrorResponse(
                503,
                "EXTERNAL_SERVICE_ERROR",
                message
        );
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public int getStatus() {
        return status;
    }

    public void setStatus(
            int status
    ) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(
            String error
    ) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message
    ) {
        this.message = message;
    }

    public String getService() {
        return service;
    }

    public void setService(
            String service
    ) {
        this.service = service;
    }

    public String getPath() {
        return path;
    }

    public void setPath(
            String path
    ) {
        this.path = path;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(
            String traceId
    ) {
        this.traceId = traceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(
            LocalDateTime timestamp
    ) {
        this.timestamp = timestamp;
    }

    public List<FieldErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(
            List<FieldErrorDetail> errors
    ) {
        this.errors = errors;
    }

    // =========================
    // INNER CLASS
    // =========================

    public static class FieldErrorDetail {

        private String field;

        private Object rejectedValue;

        private String message;

        public FieldErrorDetail() {
        }

        public FieldErrorDetail(
                String field,
                Object rejectedValue,
                String message
        ) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(
                String field
        ) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(
                Object rejectedValue
        ) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(
                String message
        ) {
            this.message = message;
        }
    }
}