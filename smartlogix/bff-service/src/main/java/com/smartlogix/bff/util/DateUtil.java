package com.smartlogix.bff.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private DateUtil() {
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =========================
    // FORMATEAR FECHA
    // =========================

    public static String format(LocalDateTime dateTime) {

        if (dateTime == null) {
            return null;
        }

        return dateTime.format(FORMATTER);
    }

    // =========================
    // FECHA ACTUAL
    // =========================

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}