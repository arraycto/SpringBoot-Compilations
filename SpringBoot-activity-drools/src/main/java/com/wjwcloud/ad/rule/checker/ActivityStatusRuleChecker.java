package com.wjwcloud.ad.rule.checker;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.client.constant.ResultCodeEnum;
import com.wjwcloud.ad.rule.base.RuleChecker;
import com.wjwcloud.ad.rule.domain.pojo.ActivityStatusRule;
import com.wjwcloud.ad.rule.domain.request.RuleCheckRequest;
import com.wjwcloud.ad.system.util.ResultUtil;
import org.springframework.stereotype.Component;

/**
 * StartTimeRuleChecker
 */
@Component
public class ActivityStatusRuleChecker implements RuleChecker<ActivityStatusRule, RuleCheckRequest> {
    public static final int PROCESSING = 3;

    @Override
    public Result check(ActivityStatusRule rule) {
        if(rule.getRule() == null){
            return ResultUtil.failResult(ResultCodeEnum.RULE_PARAM_ERROR.getCode(), "活动状态为null");
        }
        return ResultUtil.successResult(null);
    }

    @Override
    public Result<String> validate(ActivityStatusRule rule, RuleCheckRequest request) {
        if(rule.getRule() != PROCESSING){
            return ResultUtil.failResult("","");
        }
        return ResultUtil.successResult(null);
    }
}
