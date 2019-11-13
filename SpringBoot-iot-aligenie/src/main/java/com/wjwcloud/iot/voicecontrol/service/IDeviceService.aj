package com.wjwcloud.iot.voicecontrol.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @auther:zhoulei
 * @description:智能设备控制接口，根据不同的namespace调用不同的操作
 * @date :2019/5/13 11:57;
 * QQ：20971053
 */
public interface IDeviceService {
    /**
     * 设备发现
     * @param params
     * @return
     */
    JSONObject deviceDiscovery(String bodyStr);

    /**
     * 设备控制
     * @param params
     * @return
     */
    JSONObject deviceControl(String bodyStr);

    /**
     * 设备查询
     * @param params
     * @return
     */
    JSONObject deviceQuery(String bodyStr);
}
