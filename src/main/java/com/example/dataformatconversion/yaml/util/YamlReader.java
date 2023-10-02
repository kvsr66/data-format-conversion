package com.example.dataformatconversion.yaml.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.config.YamlProcessor;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.*;

public class YamlReader {

    public static void main(String[] args) throws FileNotFoundException {

        YamlReader yamlReader = new YamlReader();
//      Map<String, Object>  map = yamlReader.getJsonObjectMapFromYaml("");
//      yamlReader.getComplexFields(map);

        String path = "D:\\subbu\\workspaces\\intellij-workspace\\data-format-conversion\\src\\main\\resources\\yaml\\";
        Map<String, Object>  map1 = yamlReader.getJsonObjectMapFromYaml(path, "security.yaml");
        yamlReader.getComplexFields(map1);


    }
   public  Map<String, Object> getJsonObjectMapFromYaml(String yamlPath){

       Yaml yaml = new Yaml();

       InputStream inputStream = this.getClass().getClassLoader()
               .getResourceAsStream("security.yaml");
       Map<String, Object> objectMap = yaml.load(inputStream);

 //      System.out.println(objectMap);
       return  objectMap;
   }

    public  Map<String, Object> getJsonObjectMapFromYaml(String path, String fileName) throws FileNotFoundException {

        Yaml yaml = new Yaml();

        InputStream inputStream = new FileInputStream(path+fileName);
        Map<String, Object> objectMap = yaml.load(inputStream);

        //      System.out.println(objectMap);
        return  objectMap;
    }

   public Map<String, Object> getComplexFields(Map<String, Object> objectMap){
       Map<String, Object> complexFieldsMap = new LinkedHashMap<>();
       Map<String, Object> customFieldMappingMap = (Map<String, Object>)objectMap.get("customFieldMapping");

       if(MapUtils.isNotEmpty(customFieldMappingMap)){

           for(Map.Entry<String, Object> entry : customFieldMappingMap.entrySet()){

               String fieldName = entry.getKey();
               Map<String, Object> fieldMap = (Map<String, Object>)entry.getValue();
               String mapper = (String) fieldMap.get("mapper");
               List<String> fields = (List<String>) fieldMap.get("fields");
              // List<String> seconderyMappings = (List<String>) fieldMap.get("seconderyMapping");
                Object secObj1 = fieldMap.get("seconderyMapping");
               Map<String, Object> seconderyMappingsMap = null!=secObj1 ? (Map<String, Object>)secObj1: null;

               if(CollectionUtils.isNotEmpty(fields) && fields.size() > 1){
                   // This is complex field
                   System.out.println( "Complex field is : "+ fieldName + " with columns "+ fields);
               }

               if(MapUtils.isNotEmpty(seconderyMappingsMap) ){

                   //This is for Expression
                   System.out.println("Prepare an expression for the field: "+ fieldName);
               }

               }

       }

       return null;

   }
}
