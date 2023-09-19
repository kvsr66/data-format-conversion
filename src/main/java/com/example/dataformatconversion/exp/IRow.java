package com.example.dataformatconversion.exp;

public interface IRow {

    String get(String key);

    String[] columnsNames();

    String fetchAllValues();

}
