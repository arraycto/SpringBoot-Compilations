package com.wjwcloud.ad.rule.domain.pojo;

import com.wjwcloud.ad.rule.base.AbstractRule;

/**
 * EndTimeRule
 */
public class EndTimeRule extends AbstractRule<Long> {
    private static final long serialVersionUID = -4499210248679692849L;

    public EndTimeRule(Long endTime) {
        super(endTime);
    }

    @Override
    public String getCode() {
        return EndTimeRule.class.getSimpleName();
    }

    @Override
    public int getSort() {
        return 22;
    }

    @Override
    public String getDisplay() {
        return "活动结束时间";
    }
}
