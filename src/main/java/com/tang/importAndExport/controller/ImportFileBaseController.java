package com.tang.importAndExport.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: 生产者-消费者模式下的批量文件导入
 * @author: tangYiLong
 * @create: 2018-04-10 11:32
 **/
@Controller
@RequestMapping("/import")
public class ImportFileBaseController {
    //日志
    private Log log = LogFactory.getLog(ImportFileBaseController.class);
    //文件列表
    private Map<String, MultipartFile> fileStreams = new HashMap();
    //生产数据线程池
    private ExecutorService producerExecutor;
    //处理数据线程池
    private ExecutorService consumerExecutor;
    //有界队列，存储excel中解析出来的vo对象，元素个数大于上限时阻塞
    private ArrayBlockingQueue<Object> producerQueue;
    //队列中最后一个元素，用来标识队列结束位
    private static final String lastFlag = "lastFlag";

    @RequestMapping("/importAccess")
    public ModelAndView importAccess() {
        ModelAndView mv = new ModelAndView();
        //封装要显示到视图的数据
        mv.addObject("path", "/importXXX/import");
        //视图名
        mv.setViewName("testImport");
        return mv;
    }

    /**
     * 解析request，并生成文件列表
     */
    protected boolean parseRequestOK(HttpServletRequest request) {
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                String fileName = iter.next().toString();
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(fileName);
                fileStreams.put(fileName, file);
            }
        }
        return fileStreams.size() > 0;
    }

    /**
     * 初始化线程池，队列
     *
     * @param producerPoolSize 生产数据线程池大小
     * @param consumerPoolSize 处理数据线程池大小
     * @param queueSize        阻塞队列最大长度
     */
    protected void initSource(int producerPoolSize, int consumerPoolSize, int queueSize) {
        producerExecutor = Executors.newFixedThreadPool(producerPoolSize);
        consumerExecutor = Executors.newFixedThreadPool(consumerPoolSize);
        producerQueue = new ArrayBlockingQueue<>(queueSize);
    }

    /**
     * 启动生产线程
     *
     * @param producerClazz 线程类型
     * @param voClazz       vo类型
     */
    protected void produce(final Class producerClazz, final Class voClazz) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Map.Entry<String, MultipartFile> entry : fileStreams.entrySet()) {
                        Class[] cType = new Class[]{Class.class, InputStream.class, String.class, ArrayBlockingQueue.class};
                        Object[] cParam = new Object[]{voClazz, entry.getValue().getInputStream(), entry.getKey(), producerQueue};
                        Runnable producer = (Runnable) producerClazz.getConstructor(cType).newInstance(cParam);
                        producerExecutor.execute(producer);
                    }
                    producerExecutor.shutdown();
                    setLastFlag();
                } catch (Exception e) {
                    log.error("执行生产线程失败：" + e.getCause().getMessage());
                }
            }
        }).start();
    }

    /**
     * 启动处理线程,每获取到{batchDataSize}条数据，则开启一个线程进行处理
     *
     * @param consumerClazz 线程类型
     * @param batchDataSize 批量处理数据最大条数
     * @param batchDataSize service对象
     */
    protected void consume(Class consumerClazz, int batchDataSize, Object service) {
        try {
            boolean hasElement = true;
            while (hasElement) {
                List<Object> voList = new ArrayList<>();
                for (int i = 0; i < batchDataSize; i++) {
                    //take() 和 put() 都会阻塞,因此此处用take()，当队列中无数据可取时，将线程阻塞，防止出现空指针异常
                    Object object = producerQueue.take();
                    if (lastFlag.equals(object.toString())) {
                        hasElement = false;
                        break;
                    }
                    voList.add(object);
                }
                if (voList.size() > 0) {
                    Class[] cType = new Class[]{Object.class, List.class};
                    Object[] cParam = new Object[]{service, voList};
                    Runnable consumer = (Runnable) consumerClazz.getConstructor(cType).newInstance(cParam);
                    consumerExecutor.execute(consumer);
                }
            }
            consumerExecutor.shutdown();
        } catch (Exception e) {
            log.error("执行消费线程失败：" + e.getCause().getMessage());
        }
    }


    private void setLastFlag() throws Exception {
        while (true) {
            if (producerExecutor.isTerminated()) {
                producerQueue.put(lastFlag);
                break;
            }
            Thread.sleep(200);
        }
    }

}

