package com.wjwcloud.ad.client.request;

import com.wjwcloud.ad.client.request.base.BaseActivityPartRequest;

/**
 * 减免活动 request
 * @author JiaweiWu
 */
public class ReductionRequest extends BaseActivityPartRequest {

    public ReductionRequest(String customerRegisterId, String channelId) {
        super(customerRegisterId, channelId);
    }
}
