package com.wjwcloud.ad.rule.base;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.rule.domain.request.RuleCheckRequest;

/**
 * RuleChecker
 */
public interface RuleChecker<T extends Rule, REQ extends RuleCheckRequest> extends Checker<T> {
    /**
     * 规则检验
     *
     * @param rule
     * @param request
     * @return
     */
    Result validate(T rule, REQ request);
}
