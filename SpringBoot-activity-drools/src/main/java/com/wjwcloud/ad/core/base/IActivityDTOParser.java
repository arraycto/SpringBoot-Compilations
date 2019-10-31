
package com.wjwcloud.ad.core.base;


import com.wjwcloud.ad.client.request.base.ActivityRequest;
import com.wjwcloud.ad.core.domain.ActivityDTO;

/**
 * IActivityDTOParser
 * 活动数据传输对象解析器
 *
 * @author JiaweiWu
 */
public interface IActivityDTOParser<REQ extends ActivityRequest> {
    /**
     * 构建活动数据传输对象
     *
     * @param request
     * @return
     */
    ActivityDTO buildDTO(REQ request);
}
