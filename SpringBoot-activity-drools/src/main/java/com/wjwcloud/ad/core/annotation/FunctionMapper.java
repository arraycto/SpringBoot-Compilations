
package com.wjwcloud.ad.core.annotation;


import com.wjwcloud.ad.core.domain.FunctionCodeEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * FunctionMapper
 *
 * @author caisl
 * @since 2019-01-09
 */
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionMapper {

    /**
     * FunctionCode
     *
     * @return
     */
    FunctionCodeEnum[] value();
}
