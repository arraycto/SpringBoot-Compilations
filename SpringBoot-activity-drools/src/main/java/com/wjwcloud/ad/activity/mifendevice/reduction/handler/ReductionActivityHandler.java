package com.wjwcloud.ad.activity.mifendevice.reduction.handler;

import com.wjwcloud.ad.activity.mifendevice.reduction.domain.ReductionActivityDTO;
import com.wjwcloud.ad.activity.participate.handler.AbstractActivityPartHandler;
import com.wjwcloud.ad.core.annotation.ActivityTypeMapper;
import com.wjwcloud.ad.core.annotation.FunctionMapper;
import com.wjwcloud.ad.core.domain.ActivityTypeEnum;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.core.domain.FunctionCodeEnum;

@ActivityTypeMapper(ActivityTypeEnum.REDUCTION_ACTIVITY)
@FunctionMapper(FunctionCodeEnum.ACTIVITY_REDUCTION)
public class ReductionActivityHandler extends AbstractActivityPartHandler {

    @Override
    protected void doAction(ContextParam contextParam) {
        ReductionActivityDTO reductionActivityDTO = (ReductionActivityDTO) contextParam.getActivityDTO();
        //活动规则执行

    }
}
