package com.tang.importAndExport.controller.importControllers;

import com.tang.importAndExport.controller.ImportFileBaseController;
import com.tang.importAndExport.service.ImportXXXService;
import com.tang.importAndExport.thread.consumers.ImportXXXConsumer;
import com.tang.importAndExport.thread.producers.ImportXXXProducer;
import com.tang.importAndExport.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-10 11:34
 **/
@Slf4j
@Controller
@RequestMapping("/importXXX")
public class ImportXXXController extends ImportFileBaseController {
    //生产数据线程池大小
    private static final int PRODUCER_POOL_SIZE = 2;
    //处理数据线程池大小
    private static final int CONSUMER_POOL_SIZE = 5;
    //阻塞队列最大长度
    private static final int QUEUE_SIZE = 100000;
    //批量处理数据条数
    private static final int BATCH_PROCESS_SIZE = 2000;

    @RequestMapping("/import")
    @ResponseBody
    public String importFile(HttpServletRequest request) {
        try {
            if (parseRequestOK(request)) {
                //设置线程池和队列的大小，并初始化。可以根据实际情况调整输入值
                initSource(PRODUCER_POOL_SIZE, CONSUMER_POOL_SIZE, QUEUE_SIZE);
                //生产数据
                produce(ImportXXXProducer.class, PersonVO.class);
                //处理数据
                consume(ImportXXXConsumer.class, BATCH_PROCESS_SIZE, ImportXXXService.class);
                return "succeed";
            } else {
                //请求中未找到文件,提示重新上传
                return "failed";
            }
        } catch (Exception e) {
            log.error("导入EXCEL失败：" + e.getCause().getMessage());
            return "failed";
        }
    }
}
