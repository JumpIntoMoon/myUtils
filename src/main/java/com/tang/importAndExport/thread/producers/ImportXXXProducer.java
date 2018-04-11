package com.tang.importAndExport.thread.producers;

import com.tang.fileParse.ParseExcel;
import com.tang.importAndExport.thread.Producer;

import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:19
 **/
public class ImportXXXProducer extends Producer implements Runnable {
    public ImportXXXProducer(Class clazz, InputStream is, String fileName, ArrayBlockingQueue<Object> producerQueue) {
        super(clazz, is, fileName, producerQueue);
    }

    @Override
    public void run() {
        ParseExcel.parse(this.clazz, this.is, this.fileName, this.producerQueue);
    }

}
