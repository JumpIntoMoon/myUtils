package com.tang.importAndExport.service;

import com.tang.importAndExport.dao.CommonDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:37
 **/
@Resource
public class ImportXXXService {
    private Log log = LogFactory.getLog(ImportXXXService.class);
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
