package com.wjwcloud.oauth.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.oauth.cache.AuthStateCache;
import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.enums.AuthUserGender;
import com.wjwcloud.oauth.model.AuthCallback;
import com.wjwcloud.oauth.model.AuthToken;
import com.wjwcloud.oauth.model.AuthUser;
import com.wjwcloud.oauth.utils.UrlBuilder;
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.enums.AuthResponseStatus;
import com.wjwcloud.oauth.exception.AuthException;
import com.wjwcloud.oauth.model.AuthResponse;

/**
 * 微信登录
 */
public class AuthWeChatRequest extends AuthDefaultRequest {
    public AuthWeChatRequest(AuthConfig config) {
        super(config, AuthSource.WECHAT);
    }

    public AuthWeChatRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthSource.WECHAT, authStateCache);
    }

    /**
     * 微信的特殊性，此时返回的信息同时包含 openid 和 access_token
     *
     * @param authCallback 回调返回的参数
     * @return 所有信息
     */
    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        return this.getToken(accessTokenUrl(authCallback.getCode()));
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String openId = authToken.getOpenId();

        HttpResponse response = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(response.body());

        this.checkResponse(object);

        String location = String.format("%s-%s-%s", object.getString("country"), object.getString("province"), object.getString("city"));

        if (object.containsKey("unionid")) {
            authToken.setUnionId(object.getString("unionid"));
        }

        return AuthUser.builder()
            .username(object.getString("nickname"))
            .nickname(object.getString("nickname"))
            .avatar(object.getString("headimgurl"))
            .location(location)
            .uuid(openId)
            .gender(AuthUserGender.getRealGender(object.getString("sex")))
            .token(authToken)
            .source(source)
            .build();
    }

    @Override
    public AuthResponse refresh(AuthToken oldToken) {
        return AuthResponse.builder()
            .code(AuthResponseStatus.SUCCESS.getCode())
            .data(this.getToken(refreshTokenUrl(oldToken.getRefreshToken())))
            .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("errcode")) {
            throw new AuthException(object.getIntValue("errcode"), object.getString("errmsg"));
        }
    }

    /**
     * 获取token，适用于获取access_token和刷新token
     *
     * @param accessTokenUrl 实际请求token的地址
     * @return token对象
     */
    private AuthToken getToken(String accessTokenUrl) {
        HttpResponse response = HttpRequest.get(accessTokenUrl).execute();
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());

        this.checkResponse(accessTokenObject);

        return AuthToken.builder()
            .accessToken(accessTokenObject.getString("access_token"))
            .refreshToken(accessTokenObject.getString("refresh_token"))
            .expireIn(accessTokenObject.getIntValue("expires_in"))
            .openId(accessTokenObject.getString("openid"))
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
            .queryParam("appid", config.getClientId())
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("scope", "snsapi_login")
            .queryParam("state", getRealState(state))
            .build();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param code 授权码
     * @return 返回获取accessToken的url
     */
    @Override
    protected String accessTokenUrl(String code) {
        return UrlBuilder.fromBaseUrl(source.accessToken())
            .queryParam("code", code)
            .queryParam("appid", config.getClientId())
            .queryParam("secret", config.getClientSecret())
            .queryParam("grant_type", "authorization_code")
            .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken 用户授权后的token
     * @return 返回获取userInfo的url
     */
    @Override
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo())
            .queryParam("access_token", authToken.getAccessToken())
            .queryParam("openid", authToken.getOpenId())
            .queryParam("lang", "zh_CN")
            .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param refreshToken getAccessToken方法返回的refreshToken
     * @return 返回获取userInfo的url
     */
    @Override
    protected String refreshTokenUrl(String refreshToken) {
        return UrlBuilder.fromBaseUrl(source.refresh())
            .queryParam("appid", config.getClientId())
            .queryParam("refresh_token", refreshToken)
            .queryParam("grant_type", "refresh_token")
            .build();
    }
}
