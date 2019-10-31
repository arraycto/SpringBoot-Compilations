package com.wjwcloud.ad.common.dao;

import com.wjwcloud.ad.common.dao.mapper.ActivityConfigMapper;
import com.wjwcloud.ad.common.dataobject.ActivityConfigDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * ActivityConfigDAO
 */
@Repository
public class ActivityConfigDAO {
    @Resource
    ActivityConfigMapper activityConfigMapper;

    public ActivityConfigDO selectByPrimaryKey(Long activityId){
        return activityConfigMapper.selectByPrimaryKey(activityId);
    }

}
