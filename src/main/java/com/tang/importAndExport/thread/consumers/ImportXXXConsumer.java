package com.tang.importAndExport.thread.consumers;

import com.tang.importAndExport.service.ImportXXXService;
import com.tang.importAndExport.thread.Consumer;

import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:19
 **/
public class ImportXXXConsumer extends Consumer implements Runnable {
    public ImportXXXConsumer(Object service, List voList) {
        super(service, voList);
    }

    @Override
    public void run() {
        ImportXXXService importXXXService = (ImportXXXService) this.service;
        importXXXService.processDatas(this.voList);
    }

}
