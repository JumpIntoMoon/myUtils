package com.tang.importAndExport.service;

import com.tang.importAndExport.dao.CommonDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:37
 **/
@Slf4j
@Resource
public class ImportXXXService {
    @Autowired
    private CommonDAO dao;

    public void processDatas(List voList) {
        try {
            String sql = "";
            dao.batchInsert(sql, voList);
        } catch (Exception e) {
            log.error("EXCEL数据批量插入数据库失败：" + e.getCause().getMessage());
        }
    }
}
