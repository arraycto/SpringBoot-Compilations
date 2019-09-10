package com.wjwcloud.iot.voicecontrol.aligenie.common;


import com.wjwcloud.iot.voicecontrol.common.ConstantKey;

/**
 * Created by zhoulei on 2019/4/25.
 */
public class AligenieConstantKey extends ConstantKey {
    /**
     * 保存在session中的值，判断是否已经登陆过
     */
    public static final String MEMBER_SESSION_KEY = "MEMBER_SESSION_KEY";
    /**
     * 已登录或授权成功后调用获取token
     */
    public static final String OAUTH_CLIENT_CALLBACK = "http://wechat.tunnel.geer2.com:8047/aligenie/oauthCallback";
    public static final String OAUTH_GET_SOURCE = "http://wechat.tunnel.geer2.com:8047/aligenie/device/getResource";
}
