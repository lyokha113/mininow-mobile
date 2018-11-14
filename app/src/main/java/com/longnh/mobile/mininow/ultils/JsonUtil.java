package com.longnh.mobile.mininow.ultils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.util.Arrays;

public class JsonUtil<T> {

    private static final ObjectMapper om = new ObjectMapper();

    static {
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

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
