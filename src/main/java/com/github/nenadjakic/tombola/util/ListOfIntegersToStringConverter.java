package com.github.nenadjakic.tombola.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.Type;
import java.util.List;

@Converter
public class ListOfIntegersToStringConverter implements AttributeConverter <List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        Gson gson = new Gson();

        return gson.toJson(attribute);
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        return gson.fromJson(dbData, listType);
    }
}
