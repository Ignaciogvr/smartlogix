package com.smartlogix.bff.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    // =========================
    // METADATA
    // =========================

    private int status;

    private String message;

    private LocalDateTime timestamp;

    private String path;

    private String traceId;

    // =========================
    // DATA
    // =========================

    private T data;

    // =========================
    // CONSTRUCTORES
    // =========================

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(
            int status,
            String message,
            T data
    ) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(
            int status,
            String message,
            T data,
            String path,
            String traceId
    ) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.path = path;
        this.traceId = traceId;
        this.timestamp = LocalDateTime.now();
    }

    // =========================
    // FACTORY METHODS
    // =========================

    public static <T> ApiResponse<T> success(
            String message,
            T data
    ) {

        return new ApiResponse<>(
                200,
                message,
                data
        );
    }

    public static <T> ApiResponse<T> created(
            String message,
            T data
    ) {

        return new ApiResponse<>(
                201,
                message,
                data
        );
    }

    public static <T> ApiResponse<T> accepted(
            String message,
            T data
    ) {

        return new ApiResponse<>(
                202,
                message,
                data
        );
    }

    public static <T> ApiResponse<T> noContent(
            String message
    ) {

        return new ApiResponse<>(
                204,
                message,
                null
        );
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message
    ) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(
            LocalDateTime timestamp
    ) {
        this.timestamp = timestamp;
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

    public T getData() {
        return data;
    }

    public void setData(
            T data
    ) {
        this.data = data;
    }
}