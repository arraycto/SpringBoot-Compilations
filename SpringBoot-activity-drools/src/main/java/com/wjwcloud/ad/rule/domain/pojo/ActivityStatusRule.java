package com.wjwcloud.ad.rule.domain.pojo;

import com.wjwcloud.ad.rule.base.AbstractRule;

/**
 * ActivityStatusRule
 */
public class ActivityStatusRule extends AbstractRule<Integer> {
    @Override
    public String getCode() {
        return ActivityStatusRule.class.getSimpleName();
    }

    public ActivityStatusRule(Integer rule) {
        super(rule);
    }

    @Override
    public int getSort() {
        return 23;
    }

    @Override
    public String getDisplay() {
        return "活动状态";
    }
}
