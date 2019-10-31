package com.wjwcloud.ad.rule;

import com.wjwcloud.ad.client.constant.Result;
import com.wjwcloud.ad.BaseTest;
import com.wjwcloud.ad.rule.base.Rule;
import com.wjwcloud.ad.rule.domain.pojo.StartTimeRule;
import com.wjwcloud.ad.rule.domain.request.ActivityPartRuleRequest;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * ActivityRuleEngineTest
 */
public class ActivityRuleEngineTest extends BaseTest {
    @Resource
    private ActivityRuleEngine activityRuleEngine;


    @Test
    public void singleRuleTest(){
        ActivityPartRuleRequest request = new ActivityPartRuleRequest();
        List<Rule> ruleList = Lists.newArrayList();
        StartTimeRule startTimeRule = new StartTimeRule(System.currentTimeMillis()+100000000);
        ruleList.add(startTimeRule);
        Result<String> result = activityRuleEngine.validate(request, ruleList);
        Assert.assertTrue(result.isSuccess());
        System.out.println(result.getModel());
    }
}
