package com.wjwcloud.ad.rule.base;

import com.wjwcloud.ad.client.constant.Result;

/**
 * Checker
 */
public interface Checker<T> {
    /**
     * 规则参数格式检验
     *
     * @param rule
     * @return
     */
    Result check(T rule);
}
