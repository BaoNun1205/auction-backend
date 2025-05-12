package com.example.auction_web.utils;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VectorUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(List<Float> vector) {
        try {
            return objectMapper.writeValueAsString(vector);
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert vector to JSON", e);
        }
    }

    public static List<Float> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert JSON to vector", e);
        }
    }
}
