package com.wjwcloud.ad.provider;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.client.request.NewCustomerPartRequest;
import com.wjwcloud.ad.client.response.NewCustomerPartResponse;
import com.wjwcloud.ad.client.service.IActivityPartService;
import com.wjwcloud.ad.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * ActivityPartServiceTest
 *
 * @author caisl
 * @since 2019-02-16
 */
public class ActivityPartServiceTest extends BaseTest {

    public static final String customerRegisterId = "caisl";
    public static final String channelId = "WECHAT";
    @Resource
    private IActivityPartService activityPartService;

    @Test
    public void partNewCustomerActivity() {
        NewCustomerPartRequest request = new NewCustomerPartRequest(customerRegisterId, channelId);
        Result<NewCustomerPartResponse> result = activityPartService.partNewCustomerActivity(request);
        Assert.assertTrue(result.isSuccess());
        Assert.assertNotNull(result.getModel());
    }
}
