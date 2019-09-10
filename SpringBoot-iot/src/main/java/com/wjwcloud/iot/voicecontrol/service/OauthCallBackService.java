package com.wjwcloud.iot.voicecontrol.service;

import java.util.Map;

/**
 * @description:智能音箱登录成功后回调地址，主要根据不同的平台返回不同的回调信息
 * @date :2019/5/13 11:57;
 */

public interface OauthCallBackService {
    //根据访问平台不同返回不同数据
    public String oauthCallback(Map<String, Object> params);
}
