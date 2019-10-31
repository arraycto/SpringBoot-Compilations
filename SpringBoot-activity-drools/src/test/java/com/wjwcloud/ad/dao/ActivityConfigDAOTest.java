package com.wjwcloud.ad.dao;

import com.wjwcloud.ad.BaseTest;
import com.wjwcloud.ad.common.dao.ActivityConfigDAO;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * ActivityConfigDAOTest
 */
public class ActivityConfigDAOTest extends BaseTest {
    @Resource
    ActivityConfigDAO activityConfigDAO;

    @Test
    public void selectByIdTest(){
        activityConfigDAO.selectByPrimaryKey(0L);
    }
}
