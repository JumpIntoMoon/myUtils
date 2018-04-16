package com.tang.annotationTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @description: 利用切面实现的一个自定义注解的拦截器，可以用来做事务处理等等
 * @author: tangYiLong
 * @create: 2018-04-16 10:49
 **/
public class CustomedAnnotationAspect {
    /**
     * 日志
     */
    private static Log LOGGER = LogFactory.getLog(CustomedAnnotationAspect.class);

    /**
     * 注解拦截
     */
    @Pointcut("@annotation(com.tang.annotationTransaction.CustomedAnnotation)")
    public void annotationPointcut() {
    }

    /**
     * 方法拦截:execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)
     * 表达式中 ret-type-pattern,name-pattern, parameters pattern是必须的。
     * ret-type-pattern：可以为*表示任何返回值，全路径的类名等。
     * name-pattern：指定方法名，*代表所有，set*代表以set开头的所有方法。
     * param-pattern：指定方法参数(声明的类型)，
     * (..)代表所有参数，
     * (*)代表一个参数，
     * (*,String)代表第一个参数为任何值，第二个为String类型。
     */
    @Pointcut("execution(* com.tang.*.*(..))")
    private void methodPoincut() {
        // 这是一个标记方法
    }
    /**
     * 拦截器具体实现，使用了环绕通知
     *
     * @param pjp
     * @return result（被拦截方法的执行结果）
     */
    @Around(value = "annotationPointcut()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Class transactionClazz = null;
        try {if (targetMethod.isAnnotationPresent(CustomedAnnotation.class)) {
            //获取注解
            CustomedAnnotation customedAnnotation = targetMethod.getAnnotation(CustomedAnnotation.class);
            //获取注解传过来的值
            transactionClazz = customedAnnotation.value();
            //方法执行前······
            //do something······
            result = pjp.proceed();
            //方法执行后······
            //do something······
        }
        } catch (Exception e) {
            //方法执行失败······
            //do something······
            LOGGER.error(e.getCause().getMessage(),e);
        }
        return result;
    }

}
