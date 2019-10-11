
package com.wjwcloud.ad.activity;

import com.wjwcloud.ad.client.request.NewCustomerPartRequest;
import com.wjwcloud.ad.BaseTest;
import com.wjwcloud.ad.core.ActivityDispatcher;
import com.wjwcloud.ad.core.domain.ActivityTypeEnum;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.core.domain.FunctionCodeEnum;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * ActivityDispatcherTest
 */
public class ActivityDispatcherTest extends BaseTest {

    public static final String customerRegisterId = "caisl";
    public static final String channelId= "WECHAT";
    @Resource
    ActivityDispatcher activityDispatcher;

    @Test
    public void dispatcherTest() {
        NewCustomerPartRequest newCustomerPartRequest = new NewCustomerPartRequest(customerRegisterId, channelId);
        ContextParam contextParam = new ContextParam(FunctionCodeEnum.ACTIVITY_PARTICIPATE, newCustomerPartRequest,
                ActivityTypeEnum.NEW_CUSTOMER_GIFT.getType());
        activityDispatcher.dispatcher(contextParam);
    }

}
