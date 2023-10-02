package com.example.dataformatconversion.exp;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ExpressionUtil {

    private static String EXPRESSION_NAME = "datapipe";

//    public static void main(String[] args) throws Exception {
//
//        Map<String, String> outputExprMap = new HashMap<>();
//
//        List<TransformMapping> mappings = new TransformedMappingRepository().getTransformedMappings();
//        Map<String, TransformMapping> fieldExprMppingMap = getTransformMappingMap(mappings);
//        List<FieldMapping> fieldMappingList =  new TransformedMappingRepository().getFieldMappings();
//        Map<String, EntityMapping> entityMappingMap = new TransformedMappingRepository().getEntityMappingsMap();
//
//        List<TransformedTransaction> listTxns = new ArrayList<>();
//        TransformedTransaction txn1 = new TransformedTransaction();
//        txn1.setField1("field1");
//        txn1.setField2("field2");
//        txn1.setField3("field3");
//        listTxns.add(txn1);
//
//        for (TransformedTransaction txn : listTxns) {
//
//            Map<String, Object> fieldsValuesMap = getFieldMapFromTransformTxn(txn);
//
//            for (Map.Entry<String, Object> entry : fieldsValuesMap.entrySet()) {
//
//                String fieldName = entry.getKey();
//                String value = String.valueOf(entry.getValue());
//                String fieldId = getFieldIdByFieldNameAndEntity(fieldMappingList, fieldName, txn.getEntityName(), entityMappingMap);
//                String expressionStr = fieldExprMppingMap.get(fieldId).getExpression();
//
//                String resultExpression = createExpression(fieldName, value, expressionStr);
//
//                outputExprMap.put(fieldName, resultExpression);
//            }
//        }
//
//        System.out.println(outputExprMap);
//    }

    public static String createExpression(String varKey, String varValue, String baseExpressionStr) {
        String result = "";

        DSExpression expr = new DSExpression(EXPRESSION_NAME);

        expr.parse(baseExpressionStr);

        Map vars = new HashMap();
        vars.put(varKey, varValue);

        expr.addVars(vars);

        result = expr.evaluate(vars,  true);

        return result;
    }



}
