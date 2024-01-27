package com.qzmagic.config;

public interface Constants {
    /**
     * 数据库时间类型
     */
    String[] COLUMN_TYPE_TIME = {"datetime", "time", "date", "timestamp"};

    /**
     * 数据库数字类型
     */
    String[] COLUMN_TYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal"};


    /**
     * 数据库文本类型
     */
    public static final String[] COLUMN_TYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};


    /**
     * 数据库字符串类型
     */
    public static final String[] COLUMN_TYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};
}
