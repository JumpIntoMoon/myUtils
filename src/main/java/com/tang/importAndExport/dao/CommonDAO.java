package com.tang.importAndExport.dao;

import com.tang.importAndExport.vo.PersonVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:41
 **/
public class CommonDAO {
    private Log log = LogFactory.getLog(CommonDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int[] batchInsert(String sql, final List voList) {
        int[] updateCount = this.jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        PersonVO personVO = (PersonVO) voList.get(i);
                        ps.setString(1, personVO.getName());
                        ps.setInt(2, personVO.getAge());
                        ps.setInt(3, personVO.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return voList.size();
                    }
                });

        return updateCount;
    }

}
