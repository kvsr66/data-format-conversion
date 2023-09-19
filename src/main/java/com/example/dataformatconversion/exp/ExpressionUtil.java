package com.example.dataformatconversion.exp;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ExpressionUtil {

    private static String EXPRESSION_NAME = "datapipe";

    public static void main(String[] args) throws Exception {

        List<TransformedTransaction> listTxns = new ArrayList<>();
        List<TransformMapping> mappings = new ArrayList<>();

        Map<String, String> outputExprMap = new HashMap<>();
        Map<String, TransformMapping> fieldExprMppingMap = getTransformMappingMap(mappings);
        for (TransformedTransaction txn : listTxns) {

            Map<String, Object> fieldsValuesMap = getFieldMapFromTransformTxn(txn);

            for (Map.Entry<String, Object> entry : fieldsValuesMap.entrySet()) {

                String fieldName = entry.getKey();
                String value = String.valueOf(entry.getValue());
                String expressionStr = fieldExprMppingMap.get(fieldName).getExpression();

                String resultExpression = createExpression(fieldName, value, expressionStr);

                outputExprMap.put(fieldName, resultExpression);
            }

        }
    }

    public static String createExpression(String varKey, String varValue, String baseExpressionStr) {
        String result = "";

        DSExpression expr = new DSExpression(EXPRESSION_NAME);

        expr.parse(baseExpressionStr);

        Map vars = new HashMap();
        vars.put(varKey, varValue);

        expr.addVars(vars);

        return result;
    }


    public static Map<String, TransformMapping> getTransformMappingMap(List<TransformMapping> mappings) {
        Map<String, TransformMapping> tfmMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(mappings)) {

            for (TransformMapping mapping : mappings) {

                tfmMap.put(mapping.getToFieldId(), mapping);

            }
        }

        return tfmMap;
    }


    public static Map<String, Object> getFieldMapFromTransformTxn(TransformedTransaction transaction) throws Exception {

        Map<String, Object> fieldsAndValuesMap = new HashMap<>();

        if (null != transaction) {

            Field[] fields = TransformMapping.class.getFields();

            Map<String, Object> fieldsMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, Field::getType));

            if (MapUtils.isNotEmpty(fieldsMap)) {

                for (Map.Entry<String, Object> entry : fieldsMap.entrySet()) {

                    String fieldName = entry.getKey();
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, TransformedTransaction.class);
                    Method getter = pd.getReadMethod();
                    Object f = getter.invoke(transaction);
                    fieldsAndValuesMap.put(fieldName, f);
                }
            }

        }

        return fieldsAndValuesMap;
    }
}
