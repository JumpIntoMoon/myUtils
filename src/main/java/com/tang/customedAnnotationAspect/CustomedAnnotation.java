package com.tang.customedAnnotationAspect;

import java.lang.annotation.*;

/**
 * @description: 自定义事务注解，用来做事务处理
 * @author: tangYiLong
 * @create: 2018-04-16 10:58
 **/
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomedAnnotation {
    Class value();
}
