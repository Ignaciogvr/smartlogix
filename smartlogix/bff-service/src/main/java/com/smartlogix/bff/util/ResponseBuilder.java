package com.smartlogix.bff.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static Map<String, Object> success(Object data) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", 200);
        response.put("success", true);
        response.put("data", data);

        return response;
    }

    public static Map<String, Object> error(String message, int status) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status);
        response.put("success", false);
        response.put("error", message);

        return response;
    }
}