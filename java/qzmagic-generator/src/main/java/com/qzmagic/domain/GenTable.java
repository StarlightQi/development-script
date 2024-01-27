package com.qzmagic.domain;

import lombok.Data;


/**
 * 业务表 gen_table
 *
 * @author 笔落
 */
@Data
public class GenTable {

    /**
     * 表名称
     */
    private String tableName;


    /**
     * 表注释
     */
    private String tableComment;


    /**
     * 表创建时间
     */
    private String  createTime;


    /**
     * 表更新时间
     */
    private String updateTime;
}