package com.wjwcloud.iot.voicecontrol.aligenie.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoulei on 2019/5/7.
 */
public interface IAligenieDeviceService {

     /**
      * 天猫精灵设备发现
      * @param params
      * @return
      */
     JSONObject deviceDiscovery(String bodyStr);

     /**
      * 天猫精灵设备控制
      * @param params
      * @return
      */
     JSONObject deviceControl(String bodyStr);

     /**
      * 天猫精灵设备查询
      * @param params
      * @return
      */
     JSONObject deviceQuery(String bodyStr);

}
