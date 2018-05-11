package com.tang.importAndExport.service;

import com.tang.importAndExport.dao.CommonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:37
 **/
@Service
public class ImportXXXService {

    private Logger log = LoggerFactory.getLogger(ImportXXXService.class);

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
