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

    public static final String INVALID_CLIENT_GRANT = "VERIFY_CLIENTID_FAIL";
    public static final String INVALID_CLIENT_SECRET = "VERIFY_CLIENT_SECRET_FAIL";

    public static final String AUTH_LOCATION_OAUTH_CLIENT_AUTHORIZE = "authorize";

    public static final String TOKEN_LOCATION_OAUTH_CLIENT_ACCESS_TOKEN = "http://gzue.natapp1.cc/aligenie/accessToken";

    public static final String OAUTH_CLIENT_AUTHORIZE = "http://gzue.natapp1.cc/aligenie/authorize";
    /**
     * 已登录或授权成功后调用获取token
     */
    public static final String OAUTH_CLIENT_CALLBACK = "http://gzue.natapp1.cc/aligenie/oauthCallback";

    public static final String OAUTH_CLIENT_ACCESS_TOKEN = "http://gzue.natapp1.cc/aligenie/accessToken";

    public static final String OAUTH_GET_SOURCE = "http://gzue.natapp1.cc/aligenie/getResource";
}
