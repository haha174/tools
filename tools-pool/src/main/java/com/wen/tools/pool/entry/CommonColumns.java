package com.wen.tools.pool.entry;

import com.wen.tools.pool.annotation.FieldName;

public class CommonColumns {
    @FieldName(value = "column_name")
    private String columnName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return "CommonColumns{" +
                "columnName='" + columnName + '\'' +
                '}';
    }
}
