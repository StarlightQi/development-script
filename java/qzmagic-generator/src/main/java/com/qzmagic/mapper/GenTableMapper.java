package com.qzmagic.mapper;


import com.qzmagic.domain.GenTable;
import com.qzmagic.domain.GenTableColumn;

import java.util.List;

/**
 * 业务 数据层
 *
 * @author software
 */
public interface GenTableMapper {

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
     List<GenTable> selectDbTableList(GenTable genTable);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
     List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * 查询表字段信息
     * @param tableName 表名称
     * @return 返回表字段信息
     */
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);
}
