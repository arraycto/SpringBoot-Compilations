package com.wjwcloud.ad.rule.checker;


import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.client.constant.ResultCodeEnum;
import com.wjwcloud.ad.rule.base.RuleChecker;
import com.wjwcloud.ad.rule.domain.pojo.StartTimeRule;
import com.wjwcloud.ad.rule.domain.request.RuleCheckRequest;
import com.wjwcloud.ad.system.util.ResultUtil;
import org.springframework.stereotype.Component;

/**
 * StartTimeRuleChecker
 */
@Component
public class StartTimeRuleChecker implements RuleChecker<StartTimeRule, RuleCheckRequest> {
    @Override
    public Result check(StartTimeRule rule) {
        if(rule.getRule() == null){
            return ResultUtil.failResult(ResultCodeEnum.RULE_PARAM_ERROR.getCode(), "活动开始时间为null");
        }
        return ResultUtil.successResult(null);
    }

    @Override
    public Result<String> validate(StartTimeRule rule, RuleCheckRequest request) {
        if(rule.getRule() > System.currentTimeMillis()){
            return ResultUtil.failResult("","");
        }
        return ResultUtil.successResult(null);
    }
}
