package com.wjwcloud.iot.jwt;

public interface IJWTInfo {
    /**
     * 获取用户名
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 获取公司编号
     * @return
     */
    String getComId();
}
