package com.wjwcloud.ad.rule;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.rule.base.Rule;
import com.wjwcloud.ad.rule.base.RuleChecker;
import com.wjwcloud.ad.rule.domain.request.ActivityPartRuleRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ActivityRuleEngine
 *
 * @author caisl
 * @since 2019-01-22
 */
@Component
public class ActivityRuleEngine extends CheckerEngine<Rule, RuleChecker> {
    /**
     * 规则引擎执行方法
     *
     * @param request
     * @param rules
     * @return
     */
    public Result<String> validate(final ActivityPartRuleRequest request, List<Rule> rules) {
        return (new CheckerProcess<Rule>() {
            public Result _process(Rule rule) {
                return ActivityRuleEngine.this.getChecker(rule).validate(rule, request);
            }
        }).processSingleRule(rules);
    }

}
