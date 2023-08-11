package com.example.dataformatconversion.avro.util;

import org.apache.avro.JsonProperties;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.commons.collections.CollectionUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AvroToCsvConverterNew {

   private static int ENTITY_ID = 1000;
   private static int MAPPING_ID = 10000;
   private static int SEQUENCE_ID = 0;

    public static void main(String[] args) throws IOException {
        String schemaDir = "\\src\\main\\resources\\avro\\";
        String avroSchemaFile = schemaDir + "schemafile1.avsc";
        AvroToCsvConverterNew converter = new AvroToCsvConverterNew();
        converter.convertAvroSchemaToMetaDataCSV(avroSchemaFile, null);

    }

    public  void convertAvroSchemaToMetaDataCSV(String avroFilePath, String strSchema) throws IOException {

        String basePath = "\\src\\main\\resources\\avro\\";

        String entitySchemaFile = basePath + "Entity.csv";
        String fieldSchemaFile = basePath + "Fields.csv";
        String transformMappingFile = basePath + "transformmapping-avro.csv";
        String transformobjectFile = basePath + "transformobject-avro.csv";

        Schema avroSchema = null;

        if(null!=strSchema){
          avroSchema =  AvroConverter.getSchema(strSchema);
        }else if(null!=avroFilePath) {
            avroSchema = AvroConverter.readAvroSchemaFromSchemaFile(avroFilePath);
        }
        generateMetadata(avroSchema, entitySchemaFile, fieldSchemaFile, transformMappingFile, transformobjectFile);

    }

    public void processMetaData(Schema avroSchema, StringBuilder entityBuilder, StringBuilder fieldBuilder, StringBuilder transformMappingBuilder, StringBuilder mapperObjectBuilder ) {

          if (null != avroSchema) {

              ++ENTITY_ID;
              int entityId = ENTITY_ID;
            entityBuilder.append(buildEntityMetaData(avroSchema, null, entityId ));

            processFields(avroSchema, entityBuilder,fieldBuilder, entityId,transformMappingBuilder,mapperObjectBuilder);

//            List<Schema.Field> fields = avroSchema.getFields();
//
//            if (CollectionUtils.isNotEmpty(fields)) {
//                int localEntityId = entityId;
//
//                for (Schema.Field locaField : fields) {
//                    int localFieldId = fieldId+1;
//                    fieldBuilder.append(buildFieldsMetaData(null, locaField, localEntityId, localFieldId, transformMappingBuilder, mapperObjectBuilder));
//                    transformMappingBuilder.append(buildTransformMapper(avroSchema, locaField, localEntityId, localFieldId, ++mappingId,  ++sequenceNum, transformMappingBuilder, mapperObjectBuilder));
//
//                    if (Schema.Type.RECORD == locaField.schema().getType()) {
//                        localEntityId = ++entityId;
//                        processMetaData(locaField.schema(), null, entityBuilder, fieldBuilder, localEntityId, localFieldId, mappingId, sequenceNum, transformMappingBuilder, mapperObjectBuilder);
//                    }
//                }
//            }
            ++entityId;
        }
    }

    public StringBuilder processFields(Schema avroSchema, StringBuilder entityBuilder, StringBuilder fieldBuilder, int entityId, StringBuilder transformMappingBuilder, StringBuilder mapperObjectBuilder ){

        List<Schema.Field> fields = avroSchema.getFields();

        if (CollectionUtils.isNotEmpty(fields)) {
            int fieldId = 0;
            for (Schema.Field locaField : fields) {
                ++fieldId;
                fieldBuilder.append(buildFieldsMetaData(null, locaField, entityId, fieldId, transformMappingBuilder, mapperObjectBuilder));
                transformMappingBuilder.append(buildTransformMapper(avroSchema, locaField, entityId, fieldId, ++MAPPING_ID,  ++SEQUENCE_ID, transformMappingBuilder, mapperObjectBuilder));

                if (Schema.Type.RECORD == locaField.schema().getType()) {
                    processEntities(locaField.schema(), entityBuilder, fieldBuilder, transformMappingBuilder, mapperObjectBuilder);
                }
            }
        }

        return fieldBuilder;
    }

    public StringBuilder processEntities(Schema fieldSchema, StringBuilder entityBuilder, StringBuilder fieldBuilder, StringBuilder transformMappingBuilder, StringBuilder mapperObjectBuilder ){
        if (null != fieldSchema) {
            ++ENTITY_ID;
            int entityId = ENTITY_ID;
            entityBuilder.append(buildEntityMetaData(fieldSchema, null, entityId));

            if(CollectionUtils.isNotEmpty(fieldSchema.getFields())){
                processFields(fieldSchema, entityBuilder,fieldBuilder, entityId, transformMappingBuilder, mapperObjectBuilder);
            }
        }
      return entityBuilder;
    }

    public void generateMetadata(Schema avroSchema, String entityFilePath, String fieldsFilePath, String transformMappingfilePath, String mappingObjectFilePath) throws IOException {

        StringBuilder entityBuilder = createEntityHeader();
        StringBuilder fieldBuilder = createFieldsHeader();
        StringBuilder transformMappingBuilder = createTransformMappingHeader();
        StringBuilder transformObjectBuilder = createTransformObjectMappingHeader();


        processMetaData(avroSchema, entityBuilder, fieldBuilder, transformMappingBuilder, transformObjectBuilder );

        writeToFile(entityBuilder.toString(), entityFilePath);

        writeToFile(fieldBuilder.toString(), fieldsFilePath);

        writeToFile(transformMappingBuilder.toString(), transformMappingfilePath);

        writeToFile(transformObjectBuilder.toString(), mappingObjectFilePath);




    }

    public StringBuilder buildEntityMetaData(Schema schema, Schema.Field field, Integer entityId) {

        StringBuilder entityDataBuilder = new StringBuilder();
        if (null != schema) {
            entityDataBuilder.append(entityId).append(",");
            entityDataBuilder.append(schema.getName()).append(",");
            entityDataBuilder.append(schema.getObjectProps().get("alias")).append(",");
            entityDataBuilder.append(schema.getDoc()).append(","); //description
            entityDataBuilder.append("PK").append(",");//primary_key
            entityDataBuilder.append(schema.getNamespace()).append(","); //object_type
            entityDataBuilder.append(schema.getName()).append(","); // object_name
            entityDataBuilder.append(schema.getNamespace()).append(".").append(schema.getName()).append(","); // package_name
            entityDataBuilder.append("\n");
        } else if (null != field) {
            entityDataBuilder.append(entityId).append(",");
            entityDataBuilder.append(field.name()).append(",");
            entityDataBuilder.append(field.getObjectProp("alias")).append(",");
            entityDataBuilder.append(field.doc()).append(","); //description
            entityDataBuilder.append("PK").append(",");//primary_key
            entityDataBuilder.append(field.schema().getNamespace()).append(","); //object_type
            entityDataBuilder.append(field.name()).append(","); // object_name
            entityDataBuilder.append("\n");
        }
        return entityDataBuilder;
    }

    public StringBuilder buildFieldsMetaData(Schema schema, Schema.Field field, int entityId, int fieldId, StringBuilder mapperBuilder, StringBuilder mapperObjectBuilder) {

        StringBuilder fieldDataBuilder = new StringBuilder();
        fieldDataBuilder.append(entityId).append(","); // entity_id
        fieldDataBuilder.append(fieldId).append(","); // field_no
        fieldDataBuilder.append(entityId).append(".").append(fieldId).append(","); //"field_id
        fieldDataBuilder.append(field.name()).append(","); //field_name
        fieldDataBuilder.append(field.getObjectProps().containsValue("null") ? "Y" : "N").append(","); //is_nullable
        fieldDataBuilder.append("array".equalsIgnoreCase(field.schema().getType().getName()) ? "Y" : "N").append(","); //("is_repeated,");
        fieldDataBuilder.append("enum".equalsIgnoreCase(field.schema().getType().getName()) ? "Y" : "N").append(",");//"is_enum");
        fieldDataBuilder.append("==").append(",");// "code_type"
        fieldDataBuilder.append(getValue(field, "size")).append(",");//data_length
        fieldDataBuilder.append(getValue(field, "precision")).append(",");//data_precision
        fieldDataBuilder.append(getValue(field, "scale")).append(",");//data_scale
        fieldDataBuilder.append(getDefaultValue(field)).append(",");//default_value
        fieldDataBuilder.append(getDataType(field)).append(","); //"data_type,");
        fieldDataBuilder.append("\n");


        return fieldDataBuilder;
    }


    public static StringBuilder createEntityHeader() {

        StringBuilder entityHeader = new StringBuilder();
        entityHeader.append("entity_id,");
        entityHeader.append("entity-name,");
        entityHeader.append("alias,");
        entityHeader.append("description,");
        entityHeader.append("primary_key,");
        entityHeader.append("object_type,");
        entityHeader.append("object_name,");
        entityHeader.append("package_name,");
        entityHeader.append("\n");

        return entityHeader;
    }

    public static StringBuilder createFieldsHeader() {
        StringBuilder fieldsHeader = new StringBuilder();

        fieldsHeader.append("entity_id,");
        fieldsHeader.append("field_no,");
        fieldsHeader.append("field_id,");
        fieldsHeader.append("field_name,");
        fieldsHeader.append("is_nullable,");
        fieldsHeader.append("is_repeated,");
        fieldsHeader.append("is_enum,");
        fieldsHeader.append("code_type,");
        fieldsHeader.append("size,");
        fieldsHeader.append("precision,");
        fieldsHeader.append("scale,");
        fieldsHeader.append("default,");
        fieldsHeader.append("data_type,");
//      fieldsHeader.append("");
//      fieldsHeader.append("");
        fieldsHeader.append("\n");


        return fieldsHeader;
    }

    public static void writeToFile(String data, String filePath) throws IOException {

        FileOutputStream out = new FileOutputStream(filePath);
        out.write(data.getBytes());
        out.close();
    }

    public static String getDataType(Schema.Field field) {

        if (Schema.Type.UNION == field.schema().getType()) {
            Schema subSchema = field.schema().getTypes().get(1);
            if (Schema.Type.BYTES == subSchema.getType()) {
                LogicalType lt = subSchema.getLogicalType();
                if(null!= lt) {
                    return lt.getName();
                }else{
                    return subSchema.getType().getName();
                }
            } else {
                return subSchema.getType().getName();
            }
        } else {
            return field.schema().getType().getName();
        }
    }

public static Object getDefaultValue(Schema.Field field){

        if(field.defaultVal() instanceof JsonProperties.Null){
            return null;
       }

        return field.defaultVal();
}
    public static String getValue(Schema.Field field, String property) {

        if (Schema.Type.UNION == field.schema().getType()) {
            if (Schema.Type.BYTES == field.schema().getTypes().get(1).getType()) {
                return String.valueOf(field.schema().getTypes().get(1).getJsonProp(property));
            }
        }
        return "";
    }

    public static StringBuilder createTransformMappingHeader() {

        StringBuilder builder = new StringBuilder();
        builder.append("transform_id,");
        builder.append("map_id,");
        builder.append("to_field_id,");
        builder.append("map_type,");
        builder.append("expression,");
        builder.append("data_type,");
        builder.append("sequence_no,");
        builder.append("args,");
        builder.append("\n");

        return builder;
    }


    public static StringBuilder createTransformObjectMappingHeader() {

        StringBuilder builder = new StringBuilder();
        builder.append("transform_id,");
        builder.append("from_entity_id,");
        builder.append("to_entity_id,");
        builder.append("\n");

        return builder;
    }
    public StringBuilder buildTransformMapper(Schema schema, Schema.Field field, int entityId, int fieldId,  int mapId, int sequenceNum , StringBuilder mapperBuilder, StringBuilder mapperObjectBuilder) {

        StringBuilder transformMappingBuilder = new StringBuilder();
        transformMappingBuilder.append(entityId).append(","); // transform_id
        transformMappingBuilder.append(mapId).append(","); // map_id
        transformMappingBuilder.append(entityId).append(".").append(fieldId).append(","); //"to_field_id
        transformMappingBuilder.append("s").append(","); //map_type --To Check what actual value to assign
        transformMappingBuilder.append(schema.getName()).append(".").append(field.name()).append(","); //expression
        transformMappingBuilder.append(getDataType(field)).append(","); //"data_type,");
        transformMappingBuilder.append(sequenceNum).append(","); //sequence_no
        transformMappingBuilder.append("==").append(",");// args
        transformMappingBuilder.append("\n");

        return transformMappingBuilder;
    }


}
