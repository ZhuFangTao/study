package com.lagou.edu.anno;

import java.lang.annotation.*;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/5/6 7:50 下午
 * \
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyTransactional {

}
