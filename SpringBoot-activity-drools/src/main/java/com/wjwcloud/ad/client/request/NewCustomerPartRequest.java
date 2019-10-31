package com.wjwcloud.ad.client.request;

import com.wjwcloud.ad.client.request.base.BaseActivityPartRequest;

/**
 * NewCustomerPartRequest
 */
public class NewCustomerPartRequest extends BaseActivityPartRequest {

    /**
     * 手机号码
     */
    private String mobile;

    public NewCustomerPartRequest(String customerRegisterId, String channelId) {
        super(customerRegisterId, channelId);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
