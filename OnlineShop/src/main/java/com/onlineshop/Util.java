package com.onlineshop;

import java.util.Map;

public class Util {

    public static Map buildGenericResponse(int id, String message) {
        return Map.of(
                "id", id,
                "message", message
        );
    }

}
