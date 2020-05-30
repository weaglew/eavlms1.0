package com.eagle.eavlms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//在方法上加上此注解表示发送消息推送
@Target({ElementType.METHOD,ElementType.TYPE})  //Target说明了Annotation所修饰的对象范围 METHOD方法TYPE类
@Retention(RetentionPolicy.RUNTIME)  //注解的生命周期，表示注解会被保留到什么阶段；需要在运行时去动态获取注解信息 使用RetentionPolicy.RUNTIME
public @interface Notice {
}
