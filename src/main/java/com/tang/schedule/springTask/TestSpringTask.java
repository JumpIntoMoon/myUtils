package com.tang.schedule.springTask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring自带的Task实现定时任务
 * 采用@Scheduled注解方式实现：
 * 在applicationContext.xml中配置定时器开关和定时器注解开关，如下：
 * <!-- 配置定时器开关 里面可以配置很多参数，这里只是简单的打开task,并支持@Scheduled -->
 * <task:scheduler id="Scheduler" pool-size="10"/>
 * <!-- 配置定时器注解开关 里面可以配置很多参数，这里只是简单的打开task,并支持@Scheduled -->
 * <task:annotation-driven scheduler="Scheduler" proxy-target-class="true"/>
 * @author: tangYiLong
 * @create: 2018-05-11 9:22
 **/
@Component
public class TestSpringTask {
    /**
     * 每隔三秒触发一次
     */
    @Scheduled(cron="0/3 * * * * ?")
    public void test(){
        System.out.println("task定时任务");
    }

}
