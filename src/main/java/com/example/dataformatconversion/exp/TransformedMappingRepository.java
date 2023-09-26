package com.example.dataformatconversion.exp;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformedMappingRepository {

    public List<TransformMapping> getTransformedMappings() throws Exception {

        String path = "D:\\subbu\\workspaces\\intellij-workspace\\data-format-conversion\\src\\main\\resources\\transformmapping-avro.csv";
        List<TransformMapping> list = new ArrayList<>();

        CSVReader csvReader = new CSVReaderBuilder(new FileReader(path)).build();

        List<String[]> allData = csvReader.readAll();

        // print Data
        for (String[] row : allData) {
            TransformMapping mapping = new TransformMapping();
            mapping.setTransform_id(row[0]);
            mapping.setMap_id(row[1]);
            mapping.setTo_field_id(row[2]);
            mapping.setMap_type(row[3]);
            mapping.setExpression(row[4]);
            mapping.setData_type(row[5]);
            mapping.setSequence_no(row[6]);
            mapping.setArgs(row[7]);

            list.add(mapping);
        }


        return list;
    }

    public Map<String,EntityMapping> getEntityMappingsMap() throws Exception {

        String path = "D:\\subbu\\workspaces\\intellij-workspace\\data-format-conversion\\src\\main\\resources\\Entity.csv";
        Map<String, EntityMapping> map = new HashMap<>();

        CSVReader csvReader = new CSVReaderBuilder(new FileReader(path)).build();

        List<String[]> allData = csvReader.readAll();

        // print Data
        for (String[] row : allData) {
            EntityMapping mapping = new EntityMapping();
            mapping.setEntity_id(row[0]);
            mapping.setEntity_name(row[1]);

            map.put(mapping.getEntity_id(), mapping);
        }

        return map;
    }


    public List<FieldMapping> getFieldMappings() throws Exception {

        String path = "D:\\subbu\\workspaces\\intellij-workspace\\data-format-conversion\\src\\main\\resources\\Fields.csv";
        List<FieldMapping> list = new ArrayList<>();

        CSVReader csvReader = new CSVReaderBuilder(new FileReader(path)).build();

        List<String[]> allData = csvReader.readAll();

        // print Data
        for (String[] row : allData) {
            FieldMapping mapping = new FieldMapping();
            mapping.setEntity_id(row[0]);
            mapping.setField_no(row[1]);
            mapping.setField_id(row[2]);
            mapping.setField_name(row[3]);

            list.add(mapping);
        }

        return list;
    }

    public Map<String , FieldMapping> getFieldsMap() throws Exception {

        List<FieldMapping> fieldsList = getFieldMappings();

        Map<String , FieldMapping> map =  new HashMap<>();

        for(FieldMapping fieldMapping: fieldsList){

              map.put(fieldMapping.getField_id(), fieldMapping);
        }
        return map;
    }

}
