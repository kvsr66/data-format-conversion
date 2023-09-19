package com.example.dataformatconversion.exp;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row implements IRow{

    private final Map<String, String> mmRow;

    public Row(){mmRow = new HashMap<>();  }


    public static void extract(ResultSet aaResultSet, List<IRow> aaTable){

    }

    @Override
    public String get(String key) {
        return mmRow.get(key);
    }

    @Override
    public String[] columnsNames() {
        return new String[0];
    }

    @Override
    public String fetchAllValues() {
        return "";
    }


}
