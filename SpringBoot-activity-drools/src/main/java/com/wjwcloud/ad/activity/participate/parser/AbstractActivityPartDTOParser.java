package com.wjwcloud.ad.activity.participate.parser;


import com.wjwcloud.ad.client.constant.ResultCodeEnum;
import com.wjwcloud.ad.activity.participate.domain.BaseActivityPartDTO;
import com.wjwcloud.ad.client.request.base.BaseActivityPartRequest;
import com.wjwcloud.ad.common.dataobject.ActivityConfigDO;
import com.wjwcloud.ad.core.base.IActivityDTOParser;
import com.wjwcloud.ad.rule.base.Rule;
import com.wjwcloud.ad.system.exception.BusinessRuntimeException;

import java.util.List;

/**
 * AbstractActivityPartDTOParser
 */
public abstract class AbstractActivityPartDTOParser<REQ extends BaseActivityPartRequest> implements IActivityDTOParser<REQ> {

    /**
     * 读取数据库活动配置
     *
     * @param request
     * @return
     */
    protected abstract ActivityConfigDO queryDB(REQ request);

    /**
     * 构造活动规则集合
     *
     * @param activityConfigDO
     * @return
     */
    protected abstract List<Rule> buildRules(ActivityConfigDO activityConfigDO);

    /**
     * 组装DTO
     *
     * @param request
     * @param activityConfigDO
     * @return
     */
    protected abstract BaseActivityPartDTO assembleDTO(REQ request, ActivityConfigDO activityConfigDO);


    @Override
    public BaseActivityPartDTO buildDTO(REQ request) {
        //数据库获取活动的配置
        ActivityConfigDO activityConfigDO = queryDB(request);
        if (activityConfigDO == null) {
            throw new BusinessRuntimeException(ResultCodeEnum.RESPONSE_NULL.getCode(), ResultCodeEnum.RESPONSE_NULL.getMessage());
        }
        //创建新的BaseActivityPartDTO
        BaseActivityPartDTO activityPartDTO = assembleDTO(request, activityConfigDO);
        activityPartDTO.setRules(buildRules(activityConfigDO));
        return activityPartDTO;
    }
}
