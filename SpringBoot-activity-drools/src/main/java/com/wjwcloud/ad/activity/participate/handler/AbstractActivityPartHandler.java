package com.wjwcloud.ad.activity.participate.handler;


import com.alibaba.fastjson.JSON;
import com.wjwcloud.ad.activity.mifendevice.reduction.domain.ReductionActivityDTO;
import com.wjwcloud.ad.activity.participate.domain.NewCustomerPartDTO;
import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.client.constant.ResultCodeEnum;
import com.wjwcloud.ad.activity.participate.domain.BaseActivityPartDTO;
import com.wjwcloud.ad.client.request.base.BaseActivityPartRequest;
import com.wjwcloud.ad.client.response.base.ActivityResponse;
import com.wjwcloud.ad.core.CommonFactory;
import com.wjwcloud.ad.core.base.IActivityDTOParser;
import com.wjwcloud.ad.core.base.IActivityHandler;
import com.wjwcloud.ad.core.base.IActivityResponseParser;
import com.wjwcloud.ad.core.domain.ContextParam;
import com.wjwcloud.ad.rule.ActivityRuleEngine;
import com.wjwcloud.ad.rule.base.Rule;
import com.wjwcloud.ad.rule.domain.request.ActivityPartRuleRequest;
import com.wjwcloud.ad.system.exception.BusinessRuntimeException;
import com.wjwcloud.ad.system.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * AbstractActivityPartHandler
 */
public abstract class AbstractActivityPartHandler implements IActivityHandler {
    @Resource
    private CommonFactory commonFactory;
    @Resource
    private ActivityRuleEngine activityRuleEngine;

    @Override
    public ActivityResponse handle(ContextParam contextParam) {
        //获取对应的IActivityDTOParser类
        IActivityDTOParser activityDTOParser = commonFactory.getActivityDTOParser(contextParam.getFunctionCode(), contextParam.getActivityType());
        //获取到活动的规则 实体数据
        BaseActivityPartDTO activityDTO = (BaseActivityPartDTO) activityDTOParser.buildDTO(contextParam.getRequest());
        contextParam.setActivityDTO(activityDTO);
        //活动的校验
        doRuleCheck((BaseActivityPartRequest) contextParam.getRequest(), activityDTO.getRules());
        //执行活动
        doAction(contextParam);
        return buildResponse(contextParam);
    }

    /**
     * 构造响应对象
     *
     * @param contextParam
     * @return
     */
    private ActivityResponse buildResponse(ContextParam contextParam) {
        IActivityResponseParser parser = commonFactory.getActivityResponseParser(contextParam.getFunctionCode(),
                contextParam.getActivityType());

        ActivityResponse activityResponse = parser.buildResponse(contextParam);
        if (activityResponse == null) {
            throw new BusinessRuntimeException("", "");
        }
        return activityResponse;
    }

    /**
     * 活动规则检验
     *
     * @param rules
     */
    private void doRuleCheck(BaseActivityPartRequest activityRequest, List<Rule> rules) {
        //没有规则直接返回
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        //检查参数
        Result<List<String>> paramCheck = activityRuleEngine.check(rules);
        if (!ResultUtil.isResultSuccess(paramCheck)) {
            throw new BusinessRuntimeException(paramCheck.getResultCode(), paramCheck.getMessage());
        }
        if (!CollectionUtils.isEmpty(paramCheck.getModel())) {
            throw new BusinessRuntimeException(ResultCodeEnum.RULE_PARAM_ERROR.getCode(), JSON.toJSONString(paramCheck.getModel()));
        }
        rules.sort(Comparator.comparing(Rule::getSort));
        ActivityPartRuleRequest activityPartRuleRequest = new ActivityPartRuleRequest();
        activityPartRuleRequest.setCustomerRegisterId(activityRequest.getCustomerRegisterId());
        Result<String> result = activityRuleEngine.validate(activityPartRuleRequest, rules);
        if (!ResultUtil.isResultSuccess(result)) {
            throw new BusinessRuntimeException(result.getResultCode(), result.getMessage());
        }
        if (StringUtils.isNotBlank(result.getModel())) {
            throw new BusinessRuntimeException(ResultCodeEnum.RULE_CHECK_FAIL.getCode(), result.getModel());
        }
        return;
    }

    /**
     * 执行操作
     * 各子类自行实现，完成各种动作
     *
     * @param contextParam
     */
    protected abstract void doAction(ContextParam contextParam);


}
