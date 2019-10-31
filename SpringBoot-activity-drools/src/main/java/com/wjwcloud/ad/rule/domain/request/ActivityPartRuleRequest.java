package com.wjwcloud.ad.rule.domain.request;

/**
 * ActivityPartRuleRequest
 */
public class ActivityPartRuleRequest implements RuleCheckRequest {
    /**
     * 用户ID
     */
    private String customerRegisterId;

    public String getCustomerRegisterId() {
        return customerRegisterId;
    }

    public void setCustomerRegisterId(String customerRegisterId) {
        this.customerRegisterId = customerRegisterId;
    }
}
