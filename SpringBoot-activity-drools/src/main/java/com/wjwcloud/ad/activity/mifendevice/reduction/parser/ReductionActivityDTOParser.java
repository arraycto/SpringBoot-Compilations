package com.wjwcloud.ad.activity.mifendevice.reduction.parser;

import ch.qos.logback.classic.Level;
import com.wjwcloud.ad.activity.mifendevice.reduction.domain.ReductionActivityDTO;
import com.wjwcloud.ad.activity.participate.domain.BaseActivityPartDTO;
import com.wjwcloud.ad.activity.participate.parser.AbstractActivityPartDTOParser;
import com.wjwcloud.ad.client.request.ReductionRequest;
import com.wjwcloud.ad.client.request.base.ActivityRequest;
import com.wjwcloud.ad.client.request.base.BaseActivityPartRequest;
import com.wjwcloud.ad.client.response.ReductionResponse;
import com.wjwcloud.ad.client.response.base.ActivityResponse;
import com.wjwcloud.ad.common.constants.PresellActivityStatusEnum;
import com.wjwcloud.ad.common.dataobject.ActivityConfigDO;
import com.wjwcloud.ad.core.annotation.ActivityTypeMapper;
import com.wjwcloud.ad.core.annotation.FunctionMapper;
import com.wjwcloud.ad.core.base.IActivityResponseParser;
import com.wjwcloud.ad.core.domain.ActivityDTO;
import com.wjwcloud.ad.core.domain.ActivityTypeEnum;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.core.domain.FunctionCodeEnum;
import com.wjwcloud.ad.rule.base.Rule;
import com.wjwcloud.ad.rule.domain.pojo.ActivityStatusRule;
import com.wjwcloud.ad.rule.domain.pojo.Drools;
import com.wjwcloud.ad.rule.domain.pojo.EndTimeRule;
import com.wjwcloud.ad.rule.domain.pojo.StartTimeRule;
import com.wjwcloud.ad.system.logger.ActivityLoggerFactory;
import com.wjwcloud.ad.system.logger.ActivityLoggerMarker;
import com.wjwcloud.ad.system.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

@ActivityTypeMapper(ActivityTypeEnum.REDUCTION_ACTIVITY)
@FunctionMapper(FunctionCodeEnum.ACTIVITY_REDUCTION)
public class ReductionActivityDTOParser extends AbstractActivityPartDTOParser<ReductionRequest> implements IActivityResponseParser<ReductionRequest, ReductionActivityDTO> {

    @Override
    protected ActivityConfigDO queryDB(ReductionRequest request) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do queryDB");
        //MOCK 模拟数据，跑通流程
        ActivityConfigDO activityConfigDO = new ActivityConfigDO();
        //drools 规则
        activityConfigDO.setExtendField("");
        activityConfigDO.setStatus(PresellActivityStatusEnum.PROCESSING.getCode());
        activityConfigDO.setEndTime(System.currentTimeMillis() + 24*60*60*1000);
        activityConfigDO.setStartTime(System.currentTimeMillis() - 24*60*60*1000);
        return activityConfigDO;
    }

    @Override
    protected List<Rule> buildRules(ActivityConfigDO activityConfigDO) {
        LogUtil.log(ActivityLoggerFactory.BUSINESS, ActivityLoggerMarker.BUSINESS, Level.INFO, "do buildRules");
        List<Rule> rules = new ArrayList<>();
        rules.add(new Drools(activityConfigDO.getExtendField()));
        rules.add(new StartTimeRule(activityConfigDO.getStartTime()));
        rules.add(new EndTimeRule(activityConfigDO.getEndTime()));
        rules.add(new ActivityStatusRule(activityConfigDO.getStatus()));
        return rules;
    }

    @Override
    protected BaseActivityPartDTO assembleDTO(ReductionRequest request, ActivityConfigDO activityConfigDO) {
        return new ReductionActivityDTO();
    }

    /**
     * 构建返回响应数据
     * @param contextParam
     * @return
     */
    @Override
    public ActivityResponse buildResponse(ContextParam<ReductionRequest, ReductionActivityDTO> contextParam) {
        //todo

        return new ReductionResponse();
    }
}
