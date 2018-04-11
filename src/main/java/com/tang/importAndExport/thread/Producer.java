package com.tang.importAndExport.thread;

import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:18
 **/
public class Producer {
    protected Class clazz;
    protected InputStream is;
    protected String fileName;
    protected ArrayBlockingQueue<Object> producerQueue;

    public Producer(Class clazz, InputStream is, String fileName, ArrayBlockingQueue<Object> producerQueue) {
        this.clazz = clazz;
        this.is = is;
        this.fileName = fileName;
        this.producerQueue = producerQueue;
    }

}
