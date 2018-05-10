package com.tang.importAndExport.vo;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-11 11:02
 **/
public class PersonVO extends BaseVo {
    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
