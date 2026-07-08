package com.smartlogix.envio.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String ahora() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String formatear(LocalDateTime fecha) {
        return fecha.format(FORMATTER);
    }
}