package com.wjwcloud.ad.activity.participate.parser;

import ch.qos.logback.classic.Level;
import com.wjwcloud.ad.activity.participate.domain.BaseActivityPartDTO;
import com.wjwcloud.ad.activity.participate.domain.NewCustomerPartDTO;
import com.wjwcloud.ad.client.request.NewCustomerPartRequest;
import com.wjwcloud.ad.client.response.NewCustomerPartResponse;
import com.wjwcloud.ad.client.response.base.ActivityResponse;
import com.wjwcloud.ad.common.constants.PresellActivityStatusEnum;
import com.wjwcloud.ad.common.dataobject.ActivityConfigDO;
import com.wjwcloud.ad.core.annotation.ActivityTypeMapper;
import com.wjwcloud.ad.core.annotation.FunctionMapper;
import com.wjwcloud.ad.core.base.IActivityResponseParser;
import com.wjwcloud.ad.core.domain.ActivityTypeEnum;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.core.domain.FunctionCodeEnum;
import com.wjwcloud.ad.rule.base.Rule;
import com.wjwcloud.ad.rule.domain.pojo.ActivityStatusRule;
import com.wjwcloud.ad.rule.domain.pojo.EndTimeRule;
import com.wjwcloud.ad.rule.domain.pojo.StartTimeRule;
import com.wjwcloud.ad.system.logger.ActivityLoggerFactory;
import com.wjwcloud.ad.system.logger.ActivityLoggerMarker;
import com.wjwcloud.ad.system.util.LogUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * NewCustomerPartDTOParser
 */
@Component
@ActivityTypeMapper({ActivityTypeEnum.NEW_CUSTOMER_GIFT})
@FunctionMapper({FunctionCodeEnum.ACTIVITY_PARTICIPATE})
public class NewCustomerActivityParser extends AbstractActivityPartDTOParser<NewCustomerPartRequest> implements IActivityResponseParser<NewCustomerPartRequest, NewCustomerPartDTO> {

    @Override
    protected ActivityConfigDO queryDB(NewCustomerPartRequest request) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do queryDB");
        //MOCK 模拟数据，跑通流程
        ActivityConfigDO activityConfigDO = new ActivityConfigDO();
        activityConfigDO.setStatus(PresellActivityStatusEnum.PROCESSING.getCode());
        activityConfigDO.setEndTime(System.currentTimeMillis() + 24*60*60*1000);
        activityConfigDO.setStartTime(System.currentTimeMillis() - 24*60*60*1000);
        return activityConfigDO;
    }

    @Override
    protected List<Rule> buildRules(ActivityConfigDO activityConfigDO) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do buildRules");
        List<Rule> rules = Lists.newArrayList();
        rules.add(new StartTimeRule(activityConfigDO.getStartTime()));
        rules.add(new EndTimeRule(activityConfigDO.getEndTime()));
        rules.add(new ActivityStatusRule(activityConfigDO.getStatus()));
        return rules;
    }

    @Override
    protected BaseActivityPartDTO assembleDTO(NewCustomerPartRequest request, ActivityConfigDO activityConfigDO) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do assembleDTO");
        return new NewCustomerPartDTO();
    }


    @Override
    public ActivityResponse buildResponse(ContextParam<NewCustomerPartRequest, NewCustomerPartDTO> contextParam) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do buildResponse");
        return new NewCustomerPartResponse();
    }
}
