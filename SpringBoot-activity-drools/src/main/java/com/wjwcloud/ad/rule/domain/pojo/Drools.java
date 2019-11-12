package com.wjwcloud.ad.rule.domain.pojo;

import com.wjwcloud.ad.rule.base.AbstractRule;

public class Drools extends AbstractRule<String> {
    private String rule;

    /**
     * 构造函数的rule
     *
     * @param rule
     */
    public Drools(String rule) {
        super(rule);
    }

    @Override
    public String getCode() {
        return Drools.class.getSimpleName();
    }

    @Override
    public int getSort() {
        return 24;
    }

    @Override
    public String getDisplay() {
        return "活动规则内容";
    }
}
