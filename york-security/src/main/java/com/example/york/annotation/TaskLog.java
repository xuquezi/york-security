package com.example.york.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//被该注解注解的方法会记录task执行记录
public @interface TaskLog {
    String value() default "";
}
