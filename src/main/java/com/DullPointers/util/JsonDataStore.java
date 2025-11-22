package com.DullPointers.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonDataStore {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Setup Jackson to handle LocalDateTime and pretty printing
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static <T> void save(List<T> data, String fileName) {
        try {
            mapper.writeValue(new File(fileName), data);
        } catch (IOException e) {
            e.printStackTrace(); // In a real app, log this better
        }
    }

    public static <T> List<T> load(String fileName, Class<T[]> clazz) {
        File file = new File(fileName);
        if (!file.exists()) return new ArrayList<>(); // Return empty list if no file
        try {
            T[] arr = mapper.readValue(file, clazz);
            return new ArrayList<>(Arrays.asList(arr));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}