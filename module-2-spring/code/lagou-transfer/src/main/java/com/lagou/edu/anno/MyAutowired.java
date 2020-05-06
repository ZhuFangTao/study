package com.lagou.edu.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/5/6 7:50 下午
 * \
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {
    String beanName() default "";
}
