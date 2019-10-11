package com.wjwcloud.ad.common.dao.mapper;

import com.wjwcloud.ad.common.dataobject.ActivityConfigDO;

public interface ActivityConfigMapper {
    int deleteByPrimaryKey(Long activityConfigId);

    int insert(ActivityConfigDO record);

    int insertSelective(ActivityConfigDO record);

    ActivityConfigDO selectByPrimaryKey(Long activityConfigId);

    int updateByPrimaryKeySelective(ActivityConfigDO record);

    int updateByPrimaryKey(ActivityConfigDO record);
}