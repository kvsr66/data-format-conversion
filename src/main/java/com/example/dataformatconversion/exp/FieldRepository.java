package com.example.dataformatconversion.exp;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FieldRepository {


    public Map<String, Field> findFieldsByFieldName(String fieldName){
    List<Field> fields = new ArrayList<>();

    fields.stream().filter( field -> field.getFieldName().equalsIgnoreCase(fieldName))
            .collect(Collectors.groupingBy(field -> field.getFieldName()));



    return null;


    }

}
