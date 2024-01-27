package com.qzmagic.service;


import com.qzmagic.domain.GenTable;
import com.qzmagic.domain.GenTableColumn;

import java.io.IOException;
import java.util.List;

/**
 * 业务 服务层
 *
 * @author software
 */
public interface IGenTableService {

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
     List<GenTable> selectDbTableList(GenTable genTable);


    /**
     * 根据表名称查询表详情
     * @param tableName 表名称用逗号隔开
     * @return 表详情
     */
    List<GenTableColumn> selectTableByNames(String tableName);

    /**
     * 生成java模板简单案例
     * @param tableName 表名称
     * @return 返回结果
     */
    String createJavaTemplate(String tableName) throws IOException;
}
