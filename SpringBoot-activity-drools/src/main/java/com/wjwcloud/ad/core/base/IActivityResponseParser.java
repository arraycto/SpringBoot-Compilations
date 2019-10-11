package com.wjwcloud.ad.core.base;

import com.wjwcloud.ad.client.request.base.ActivityRequest;
import com.wjwcloud.ad.client.response.base.ActivityResponse;
import com.wjwcloud.ad.core.domain.ActivityDTO;
import com.wjwcloud.ad.core.domain.ContextParam;

/**
 * IActivityResponseParser
 * 活动响应对象解析器
 * 主要完成Response的组装
 * @author JiaweiWu
 */
public interface IActivityResponseParser<REQ extends ActivityRequest, DTO extends ActivityDTO> {
    /**
     * 构造响应对象
     *
     * 此处采用泛型设计，是因为实际过程中发现需要做两次类转换，为了减少转换的代码
     * 但是只有一个方法用到了，感觉又有点鸡肋
     * 也可以用多态和对象转换替代泛型
     *
     * @param contextParam
     * @return
     */
    ActivityResponse buildResponse(ContextParam<REQ, DTO> contextParam);
}
