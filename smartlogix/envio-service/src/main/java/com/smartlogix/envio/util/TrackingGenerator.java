package com.smartlogix.envio.util;

import java.util.UUID;

public class TrackingGenerator {

    public static String generate() {
        return "SLX-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}