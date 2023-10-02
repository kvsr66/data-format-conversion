package com.example.dataformatconversion.service;

import com.example.dataformatconversion.controller.ExpressionRequest;
import com.example.dataformatconversion.exp.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetadataExpressionService {

    public Map<String, String> processExpression(ExpressionRequest request) throws Exception {

        String entityName = request.getEntityName() ;
        List entityObjList = request.getRequestObjectList()
                ;
        return getEnrichedExpressions(entityName, entityObjList);
    }

    Map<String, String> getEnrichedExpressions(String entityName , List entityObjList) throws Exception {

        Map<String, String> outputExprMap = new HashMap<>();

        List<TransformMapping> mappings = new TransformedMappingRepository().getTransformedMappings();

        Map<String, TransformMapping> fieldExprMppingMap = getTransformMappingMap(mappings);

        List<FieldMapping> fieldMappingList =  new TransformedMappingRepository().getFieldMappings();

        Map<String, EntityMapping> entityMappingMap = new TransformedMappingRepository().getEntityMappingsMap();

//        List<Object> listTxns = new ArrayList<>();
//        TransformedTransaction txn1 = new TransformedTransaction();
//        txn1.setField1("field1");
//        txn1.setField2("field2");
//        txn1.setField3("field3");
//        listTxns.add(txn1);

        for (Object txn : entityObjList) {

            Map<String, Object> fieldsValuesMap = getFieldMapFromTransformTxn(txn);

            for (Map.Entry<String, Object> entry : fieldsValuesMap.entrySet()) {

                String fieldName = entry.getKey();
                String value = String.valueOf(entry.getValue());
                String fieldId = getFieldIdByFieldNameAndEntity(fieldMappingList, fieldName, entityName, entityMappingMap);
                String expressionStr = fieldExprMppingMap.get(fieldId).getExpression();

                String resultExpression = ExpressionUtil.createExpression(fieldName, value, expressionStr);

                outputExprMap.put(fieldName, resultExpression);
            }
        }

        System.out.println(outputExprMap);

        return outputExprMap;

    }


    public static Map<String, TransformMapping> getTransformMappingMap(List<TransformMapping> mappings) {
        Map<String, TransformMapping> tfmMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(mappings)) {

            for (TransformMapping mapping : mappings) {

                tfmMap.put(mapping.getTo_field_id(), mapping);

            }
        }

        return tfmMap;
    }

    public static Map<String, Object> getFieldMapFromTransformTxn(Object transactionObj) throws Exception {

        Map<String, Object> fieldsAndValuesMap = new HashMap<>();

        if (null != transactionObj) {

            java.lang.reflect.Field[] fields = TransformedTransaction.class.getDeclaredFields();

            Map<String, Object> fieldsMap = Arrays.stream(fields).collect(Collectors.toMap(java.lang.reflect.Field::getName, Field::getType));

            if (MapUtils.isNotEmpty(fieldsMap)) {

                for (Map.Entry<String, Object> entry : fieldsMap.entrySet()) {

                    String fieldName = entry.getKey();
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, TransformedTransaction.class);
                    Method getter = pd.getReadMethod();
                    Object f = getter.invoke(transactionObj);
                    fieldsAndValuesMap.put(fieldName, f);
                }
            }

        }

        return fieldsAndValuesMap;
    }

    public static String getFieldIdByFieldNameAndEntity(List<FieldMapping> fieldMappingList, String field, String entityName, Map<String, EntityMapping> entityMap){

        String fieldId = "";

        for(FieldMapping fieldMapping : fieldMappingList){

            if(field.equalsIgnoreCase(fieldMapping.getField_name())){
                String entityNameFromMap = entityMap.get(fieldMapping.getEntity_id()).getEntity_name();
                if(StringUtils.equalsIgnoreCase(entityNameFromMap, entityName)){
                    fieldId = fieldMapping.getField_id();
                }
            }
        }

        return fieldId;

    }
}
