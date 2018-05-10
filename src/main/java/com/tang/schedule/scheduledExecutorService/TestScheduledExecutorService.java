package com.tang.schedule.scheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledExecutorService是从Java SE5的java.util.concurrent里做为并发工具类被引进的，
 * 这是最理想的定时任务实现方式。相比于上timer和threadWait，它有以下好处：
 * 1>相比于Timer的单线程，它是通过线程池的方式来执行任务的
 * 2>可以很灵活的去设定第一次执行任务delay时间
 * 3>提供了良好的约定，以便设定执行的时间间隔
 *
 * @author: tangYiLong
 * @create: 2018-05-09 10:30
 **/
@Component
public class TestScheduledExecutorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //通过静态方法创建ScheduledExecutorService的实例
    //多个线程的：Executors.newScheduledThreadPool()，单线程的：newSingleThreadScheduledExecutor()
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);
    private ScheduledExecutorService sScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public void executeSingle() {
        final Map map = jdbcTemplate.queryForMap("select * from person limit 1");
        //单线程池的循环任务
        sScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(map.toString());
            }
            //第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        }, 10, 1, TimeUnit.SECONDS);
    }

    public void executeSeveral() {
        // 延时任务
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("first task");
            }
        }, 1, TimeUnit.SECONDS);

        // 循环任务，按照上一次任务的发起时间计算下一次任务的开始时间
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleAtFixedRate:" + System.currentTimeMillis() / 1000);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        }, 1, 1, TimeUnit.SECONDS);

        // 循环任务，以上一次任务的结束时间计算下一次任务的开始时间
        mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleWithFixedDelay:" + System.currentTimeMillis() / 1000);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        }, 1, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello !!");
            }
        }, 10, 1, TimeUnit.SECONDS);
    }
}
