package br.com.nubank.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class JsonParse {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T jsonToObject(String json, TypeReference<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch(Exception e) {
            System.out.printf("Erro at JsonParse.jsonToObject. Details: %s", e.getMessage());
        }
        return null;
    }

    public static <T> String objectToJson(List<T> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch(Exception e) {
            System.out.printf("Erro at JsonParse.jsonToObject. Details: %s", e.getMessage());
        }
        return null;
    }
}
