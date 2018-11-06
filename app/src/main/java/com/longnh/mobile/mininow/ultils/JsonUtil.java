package com.longnh.mobile.mininow.ultils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil<T> {

    private static final ObjectMapper om = new ObjectMapper();

    public static String getJson(Object input) {
        try {
            return om.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T getObject(String json, Class<T> cls) {
        try {
            return om.readValue(json, cls);
        } catch (IOException e) {
            return null;
        }
    }
}
