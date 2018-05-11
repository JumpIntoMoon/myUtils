package com.tang.schedule.quartz;

import org.springframework.stereotype.Component;

/**
 * 任务调度类
 * 任务调度类的启动方式：
 * 在applicationContext.xml配置定时任务tomcat加载的初始化类，加载时运行execute方法：
 * <bean id="testQuartz" class="com.tang.schedule.quartz.TestQuartz" init-method="execute"></bean>
 * 这样在spring容器启动时就会调用该方法
 * @author: tangYiLong
 * @create: 2018-05-09 11:19
 **/
@Component
public class TestQuartz {

    /**
     * @param jobName job名称
     * @param time    cron表达式
     */
    public void execute(String jobName, String time) {
        //addJob(任务名称, 具体需要完成的任务, 时间间隔 )
        //CronTrigger配置格式:[秒] [分] [小时] [日] [月] [周] [年]
        //0 0 12 * * ?              每天12点触发
        //0 15 10 ? * *             每天10点15分触发
        //0 15 10 * * ?             每天10点15分触发
        //0 15 10 * * ? *           每天10点15分触发
        //0 15 10 * * ? 2005        2005年每天10点15分触发
        //0 * 14 * * ?              每天下午的 2点到2点59分每分触发
        //0 0/5 14 * * ?            2点到2点59分(整点开始，每隔5分触发)
        //0 0/5 14,18 * * ?         每天下午的 2点到2点59分(整点开始，每隔5分触发)每天下午的 18点到18点59分(整点开始，每隔5分触发)
        //0 0-5 14 * * ?            每天下午的 2点到2点05分每分触发
        //0 10,44 14 ? 3 WED        3月分每周三下午的 2点10分和2点44分触发
        //0 15 10 ? * MON-FRI       从周一到周五每天上午的10点15分触发
        //0 15 10 15 * ?            每月15号上午10点15分触发
        //0 15 10 L * ?             每月最后一天的10点15分触发
        //0 15 10 ? * 6L            每月最后一周的星期五的10点15分触发
        //0 15 10 ? * 6L 2002-2005  从2002年到2005年每月最后一周的星期五的10点15分触发
        //0 15 10 ? * 6#3           每月的第三周的星期五开始触发
        //0 0 12 1/5 * ?            每月的第一个中午开始每隔5天触发一次
        //0 11 11 11 11 ?           每年的11月11号 11点11分触发(光棍节)
        QuartzManager.addJob(jobName, SimpleJob.class, time);
    }

    public static void main(String[] args) {
        QuartzManager.addJob("Query persons", SimpleJob.class, "0/5 * * * * ?");
    }
}
