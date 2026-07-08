package com.smartlogix.bff.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;

public class MapperUtil {

    private MapperUtil() {
    }

    // =========================
    // MAPEAR OBJETO
    // =========================

    public static <T, R> R map(T source, Function<T, R> mapper) {

        if (source == null) {
            return null;
        }

        return mapper.apply(source);
    }

    // =========================
    // MAPEAR LISTAS
    // =========================

    public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {

        if (list == null) {
            return List.of();
        }

        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}