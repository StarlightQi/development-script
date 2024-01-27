package com.qzmagic.service.impl;

import com.qzmagic.domain.GenTable;
import com.qzmagic.domain.GenTableColumn;
import com.qzmagic.mapper.GenTableMapper;
import com.qzmagic.service.IGenTableService;
import com.qzmagic.utlis.DBUtils;
import com.qzmagic.utlis.FileUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务 服务层实现
 *
 * @author software
 */
@Service
@AllArgsConstructor
public class GenTableServiceImpl implements IGenTableService {
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);
    private GenTableMapper genTableMapper;

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        return genTableMapper.selectDbTableList(genTable);
    }

    @Override
    public List<GenTableColumn> selectTableByNames(String tableName) {
        if (tableName == null || tableName.equals("")) {
            return null;
        }
        return genTableMapper.selectDbTableColumnsByName(tableName);
    }

    /**
     * @param tableName 表名称
     * @return
     */
    @Override
    public String createJavaTemplate(String tableName) throws IOException {
        String text = FileUtils.readFileFromClasspath("template/java.txt");
        List<GenTableColumn> genTableColumns = genTableMapper.selectDbTableColumnsByName(tableName);
        List<GenTable> genTables = genTableMapper.selectDbTableListByNames(new String[]{tableName});
        if (CollectionUtils.isEmpty(genTableColumns)||genTables.size()<1) {
            return "查询内容为空";
        }
        Map<String, String> map = new HashMap<>();
        map.put("tableName",DBUtils.toPascalCase(genTables.get(0).getTableName()));
        map.put("tableComment",genTables.get(0).getTableComment());
        map.put("columnListStr",genTableColumns.stream().map(this::columnToString).collect(Collectors.joining("\n\n")));
        return mapToString(map,text);
    }


    private String columnToString(GenTableColumn column) {
        Map<String, String> map = new HashMap<>();
        map.put("type", DBUtils.sqlTypeToJavaType(column.getColumnType()));
        map.put("columnComment", column.getColumnComment());
        map.put("columnName", DBUtils.toCamelCase(column.getColumnName()));
        return mapToString(map, "    /**\n" +
                "     * ${columnComment}\n" +
                "     */\n" +
                "    private ${type} ${columnName};");
    }


    private String mapToString(Map<String, String> map, String template) {
        StringSubstitutor substitutor = new StringSubstitutor(map);
        return substitutor.replace(template);
    }
}