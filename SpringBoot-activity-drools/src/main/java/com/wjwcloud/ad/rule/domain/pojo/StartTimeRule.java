package com.wjwcloud.ad.rule.domain.pojo;

import com.wjwcloud.ad.rule.base.AbstractRule;

/**
 * StartTimeRule
 */
public class StartTimeRule extends AbstractRule<Long> {
    /**
     * 构造函数的rule
     *
     * @param rule
     */
    public StartTimeRule(Long rule) {
        super(rule);
    }

    public Long getStartTime() {
        return this.getRule();
    }

    @Override
    public String getCode() {
        return StartTimeRule.class.getSimpleName();
    }

    @Override
    public int getSort() {
        return 21;
    }

    @Override
    public String getDisplay() {
        return "活动开始时间";
    }
}
