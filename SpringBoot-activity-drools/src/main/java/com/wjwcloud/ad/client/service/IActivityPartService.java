package com.wjwcloud.ad.client.service;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.client.request.NewCustomerPartRequest;
import com.wjwcloud.ad.client.response.NewCustomerPartResponse;

/**
 * IActivityPartService
 */
public interface IActivityPartService {
    /**
     * 参与新人有礼活动
     *
     * @param request
     * @return
     */
    Result<NewCustomerPartResponse> partNewCustomerActivity(NewCustomerPartRequest request);

}
