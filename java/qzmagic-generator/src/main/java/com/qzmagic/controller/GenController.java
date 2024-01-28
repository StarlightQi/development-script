package com.qzmagic.controller;

import com.qzmagic.domain.GenTable;
import com.qzmagic.domain.GenTableColumn;
import com.qzmagic.service.IGenTableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 代码生成 操作处理
 *
 * @author software
 */
@RestController
@RequestMapping("/db")
@AllArgsConstructor
public class GenController {
    private IGenTableService genTableService;
    /**
     * 查询数据库列表
     */
    @GetMapping("/list")
    public List<GenTable> dataList(GenTable genTable) {
        return genTableService.selectDbTableList(genTable);
    }


    @GetMapping("/table")
    public List<GenTableColumn> selectTable(String tableNames){
        return genTableService.selectTableByNames(tableNames);
    }


    @GetMapping("/createJavaTemplate")
    public String createJavaTemplate(String tableName) throws IOException {
        return genTableService.createJavaTemplate(tableName);
    }


}