package com.tang.importAndExport.thread;

import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 10:18
 **/
public class Consumer {
    protected Object service;
    protected List voList;

    public Consumer(Object service, List voList) {
        this.service = service;
        this.voList = voList;
    }

}
