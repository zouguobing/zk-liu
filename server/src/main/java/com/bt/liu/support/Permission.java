package com.bt.liu.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by binglove on 16/3/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    //用户名权限 多个以 '|' 分隔  例如 "admin|devAdmin"
    String userName() default "admin";
}
