package com.wjwcloud.elk.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author  JiaweiWu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    boolean isLog() default true;

}