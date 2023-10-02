package com.example.dataformatconversion;

import com.example.dataformatconversion.exp.TransformedTransaction;
import org.apache.commons.collections.MapUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjTest {

    public static void main(String[] args) throws Exception {

        Employee emp = new Employee();
        emp.setCourse("COURSE");
        emp.setName("NAME");
        printObj(new Employee());

        Map<String, Object> map = getFieldMapFromTransformTxn(emp);

        System.out.println(map);






    }

    public static void printObj(Object obj) {

        System.out.println(obj.getClass());

    }


    public static Map<String, Object> getFieldMapFromTransformTxn(Object transaction) throws Exception {

        Map<String, Object> fieldsAndValuesMap = new HashMap<>();

        if (null != transaction) {

            Field[] fields = transaction.getClass().getDeclaredFields();

            Map<String, Object> fieldsMap = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, Field::getType));

            if (MapUtils.isNotEmpty(fieldsMap)) {

                for (Map.Entry<String, Object> entry : fieldsMap.entrySet()) {

                    String fieldName = entry.getKey();
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, transaction.getClass());
                    Method getter = pd.getReadMethod();
                    Object f = getter.invoke(transaction);
                    fieldsAndValuesMap.put(fieldName, f);
                }
            }

        }

        return fieldsAndValuesMap;
    }
}
class Employee{
  private String name;
  private String course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}