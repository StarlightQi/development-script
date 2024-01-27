package com.qzmagic.domain;

import lombok.Data;


/**
 * 代码生成业务字段表 gen_table_column
 *
 * @author 笔落
 */
@Data
public class GenTableColumn {

    /**
     * 是否是必填的 1 是 0 不是
     */
    private Integer isRequired;


    /**
     * 是否是主键 1 是 0 不是
     */
    private Integer isMainKey;

    /**
     * 排序
     */
    private Integer sort;


    /***
     * 行的注释
     */
    private String columnComment;


    /**
     * 是否是自增1 是 0 不是
     */
    private Integer isIncrement;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 字段名称
     */
    private String columnName;

}
