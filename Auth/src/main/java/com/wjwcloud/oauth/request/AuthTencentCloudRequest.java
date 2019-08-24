package com.wjwcloud.oauth.request;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.oauth.model.AuthCallback;
import com.wjwcloud.oauth.model.AuthUser;
import com.wjwcloud.oauth.utils.UrlBuilder;
import com.wjwcloud.oauth.cache.AuthStateCache;
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.enums.AuthUserGender;
import com.wjwcloud.oauth.exception.AuthException;
import com.wjwcloud.oauth.model.AuthToken;

/**
 * 腾讯云登录
 */
public class AuthTencentCloudRequest extends AuthDefaultRequest {

    public AuthTencentCloudRequest(AuthConfig config) {
        super(config, AuthSource.TENCENT_CLOUD);
    }

    public AuthTencentCloudRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthSource.TENCENT_CLOUD, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        HttpResponse response = doGetAuthorizationCode(authCallback.getCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());
        this.checkResponse(accessTokenObject);
        return AuthToken.builder()
            .accessToken(accessTokenObject.getString("access_token"))
            .expireIn(accessTokenObject.getIntValue("expires_in"))
            .refreshToken(accessTokenObject.getString("refresh_token"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpResponse response = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(response.body());
        this.checkResponse(object);

        object = object.getJSONObject("data");
        return AuthUser.builder()
            .uuid(object.getString("id"))
            .username(object.getString("name"))
            .avatar("https://dev.tencent.com/" + object.getString("avatar"))
            .blog("https://dev.tencent.com/" + object.getString("path"))
            .nickname(object.getString("name"))
            .company(object.getString("company"))
            .location(object.getString("location"))
            .gender(AuthUserGender.getRealGender(object.getString("sex")))
            .email(object.getString("email"))
            .remark(object.getString("slogan"))
            .token(authToken)
            .source(source)
            .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.getIntValue("code") != 0) {
            throw new AuthException(object.getString("msg"));
        }
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
            .queryParam("scope", "user")
            .queryParam("state", getRealState(state))
            .build();
    }
}
