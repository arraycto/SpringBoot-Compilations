package com.wjwcloud.oauth.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.oauth.cache.AuthStateCache;
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.enums.AuthUserGender;
import com.wjwcloud.oauth.exception.AuthException;
import com.wjwcloud.oauth.model.AuthCallback;
import com.wjwcloud.oauth.model.AuthToken;
import com.wjwcloud.oauth.model.AuthUser;
import com.wjwcloud.oauth.utils.GlobalAuthUtil;
import com.wjwcloud.oauth.utils.UrlBuilder;

/**
 * Stack Overflow登录
 */
public class AuthStackOverflowRequest extends AuthDefaultRequest {

    public AuthStackOverflowRequest(AuthConfig config) {
        super(config, AuthSource.STACK_OVERFLOW);
    }

    public AuthStackOverflowRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthSource.STACK_OVERFLOW, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String accessTokenUrl = accessTokenUrl(authCallback.getCode());
        HttpResponse response = HttpRequest.post(accessTokenUrl)
            .contentType("application/x-www-form-urlencoded")
            .form(GlobalAuthUtil.parseQueryToMap(accessTokenUrl))
            .execute();
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());
        this.checkResponse(accessTokenObject);

        return AuthToken.builder()
            .accessToken(accessTokenObject.getString("access_token"))
            .expireIn(accessTokenObject.getIntValue("expires"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String userInfoUrl = UrlBuilder.fromBaseUrl(this.source.userInfo())
            .queryParam("access_token", authToken.getAccessToken())
            .queryParam("site", "stackoverflow")
            .queryParam("key", this.config.getStackOverflowKey())
            .build();
        HttpResponse response = HttpRequest.get(userInfoUrl).execute();
        JSONObject object = JSONObject.parseObject(response.body());
        this.checkResponse(object);
        JSONObject userObj = object.getJSONArray("items").getJSONObject(0);

        return AuthUser.builder()
            .uuid(userObj.getString("user_id"))
            .avatar(userObj.getString("profile_image"))
            .location(userObj.getString("location"))
            .nickname(userObj.getString("display_name"))
            .blog(userObj.getString("website_url"))
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
            .queryParam("scope", "read_inbox")
            .queryParam("state", getRealState(state))
            .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
    }
}
