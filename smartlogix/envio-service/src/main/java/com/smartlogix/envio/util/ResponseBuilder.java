package com.smartlogix.envio.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    public static Map<String, Object> ok(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", message);
        return response;
    }
}