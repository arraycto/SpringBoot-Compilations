package com.wjwcloud.ad.activity.participate.handler;


import ch.qos.logback.classic.Level;
import com.wjwcloud.ad.core.annotation.ActivityTypeMapper;
import com.wjwcloud.ad.core.annotation.FunctionMapper;
import com.wjwcloud.ad.core.domain.ActivityTypeEnum;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.core.domain.FunctionCodeEnum;
import com.wjwcloud.ad.process.ActivityRecordProcess;
import com.wjwcloud.ad.process.RewardProcess;
import com.wjwcloud.ad.system.logger.ActivityLoggerFactory;
import com.wjwcloud.ad.system.logger.ActivityLoggerMarker;
import com.wjwcloud.ad.system.util.LogUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * NewCustomerPartHandler
 */
@Component
@ActivityTypeMapper({ActivityTypeEnum.NEW_CUSTOMER_GIFT})
@FunctionMapper({FunctionCodeEnum.ACTIVITY_PARTICIPATE})
public class NewCustomerPartHandler extends AbstractActivityPartHandler {

    @Resource
    private ActivityRecordProcess activityRecordProcess;
    @Resource
    private RewardProcess rewardProcess;

    @Override
    protected void doAction(ContextParam contextParam) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do doAction");
        rewardProcess.reward();
        activityRecordProcess.insertRecord();
    }


}
