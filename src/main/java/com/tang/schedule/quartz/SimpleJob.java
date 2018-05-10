package com.tang.schedule.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 被执行的任务类
 *
 * @author: tangYiLong
 * @create: 2018-05-09 11:13
 **/
@Slf4j
public class SimpleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //添加具体任务实现
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        log.debug(sdf.format(new Date()));
        System.out.println(sdf.format(new Date()));
    }
}
