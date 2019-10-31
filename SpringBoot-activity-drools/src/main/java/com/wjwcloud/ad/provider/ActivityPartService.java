package com.wjwcloud.ad.provider;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.client.request.NewCustomerPartRequest;
import com.wjwcloud.ad.client.response.NewCustomerPartResponse;
import com.wjwcloud.ad.client.service.IActivityPartService;
import com.wjwcloud.ad.core.ActivityDispatcher;
import com.wjwcloud.ad.core.domain.ActivityTypeEnum;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.core.domain.FunctionCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ActivityPartService
 */
@Service
public class ActivityPartService implements IActivityPartService {
    @Resource
    private ActivityDispatcher activityDispatcher;


    @Override
    public Result<NewCustomerPartResponse> partNewCustomerActivity(NewCustomerPartRequest request) {
        ContextParam contextParam = new ContextParam(FunctionCodeEnum.ACTIVITY_PARTICIPATE, request,
                ActivityTypeEnum.NEW_CUSTOMER_GIFT.getType());
        return activityDispatcher.dispatcher(contextParam);
    }
}
