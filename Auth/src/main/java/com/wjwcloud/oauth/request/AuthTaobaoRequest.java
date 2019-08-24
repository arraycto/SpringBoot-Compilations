package com.wjwcloud.oauth.request;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.oauth.cache.AuthStateCache;
import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.enums.AuthUserGender;
import com.wjwcloud.oauth.model.AuthCallback;
import com.wjwcloud.oauth.model.AuthToken;
import com.wjwcloud.oauth.model.AuthUser;
import com.wjwcloud.oauth.utils.GlobalAuthUtil;
import com.wjwcloud.oauth.utils.UrlBuilder;
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.exception.AuthException;

/**
 * 淘宝登录
 */
public class AuthTaobaoRequest extends AuthDefaultRequest {

    public AuthTaobaoRequest(AuthConfig config) {
        super(config, AuthSource.TAOBAO);
    }

    public AuthTaobaoRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthSource.TAOBAO, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        return AuthToken.builder().accessCode(authCallback.getCode()).build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpResponse response = doPostAuthorizationCode(authToken.getAccessCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());
        if (accessTokenObject.containsKey("error")) {
            throw new AuthException(accessTokenObject.getString("error_description"));
        }
        authToken.setAccessToken(accessTokenObject.getString("access_token"));
        authToken.setRefreshToken(accessTokenObject.getString("refresh_token"));
        authToken.setExpireIn(accessTokenObject.getIntValue("expires_in"));
        authToken.setUid(accessTokenObject.getString("taobao_user_id"));
        authToken.setOpenId(accessTokenObject.getString("taobao_open_uid"));

        String nick = GlobalAuthUtil.urlDecode(accessTokenObject.getString("taobao_user_nick"));
        return AuthUser.builder()
            .uuid(accessTokenObject.getString("taobao_user_id"))
            .username(nick)
            .nickname(nick)
            .gender(AuthUserGender.UNKNOWN)
            .token(authToken)
            .source(source)
            .build();
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     * @since 1.9.3
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
            .queryParam("response_type", "code")
            .queryParam("client_id", config.getClientId())
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("view", "web")
            .queryParam("state", getRealState(state))
            .build();
    }
}
