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
import com.wjwcloud.oauth.enums.AuthResponseStatus;
import com.wjwcloud.oauth.model.AuthResponse;

/**
 * Teambition授权登录
 */
public class AuthTeambitionRequest extends AuthDefaultRequest {

    public AuthTeambitionRequest(AuthConfig config) {
        super(config, AuthSource.TEAMBITION);
    }

    public AuthTeambitionRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthSource.TEAMBITION, authStateCache);
    }

    /**
     * @param authCallback 回调返回的参数
     * @return 所有信息
     */
    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        HttpResponse response = HttpRequest.post(source.accessToken())
            .form("client_id", config.getClientId())
            .form("client_secret", config.getClientSecret())
            .form("code", authCallback.getCode())
            .form("grant_type", "code")
            .execute();
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());

        this.checkResponse(accessTokenObject);

        return AuthToken.builder()
            .accessToken(accessTokenObject.getString("access_token"))
            .refreshToken(accessTokenObject.getString("refresh_token"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String accessToken = authToken.getAccessToken();

        HttpResponse response = HttpRequest.get(source.userInfo())
            .header("Authorization", "OAuth2 " + accessToken)
            .execute();
        JSONObject object = JSONObject.parseObject(response.body());

        this.checkResponse(object);

        authToken.setUid(object.getString("_id"));

        return AuthUser.builder()
            .uuid(object.getString("_id"))
            .username(object.getString("name"))
            .nickname(object.getString("name"))
            .avatar(object.getString("avatarUrl"))
            .blog(object.getString("website"))
            .location(object.getString("location"))
            .email(object.getString("email"))
            .gender(AuthUserGender.UNKNOWN)
            .token(authToken)
            .source(source)
            .build();
    }

    @Override
    public AuthResponse refresh(AuthToken oldToken) {
        String uid = oldToken.getUid();
        String refreshToken = oldToken.getRefreshToken();
        HttpResponse response = HttpRequest.post(source.refresh())
            .form("_userId", uid)
            .form("refresh_token", refreshToken)
            .execute();
        JSONObject refreshTokenObject = JSONObject.parseObject(response.body());

        this.checkResponse(refreshTokenObject);

        return AuthResponse.builder()
            .code(AuthResponseStatus.SUCCESS.getCode())
            .data(AuthToken.builder()
                .accessToken(refreshTokenObject.getString("access_token"))
                .refreshToken(refreshTokenObject.getString("refresh_token"))
                .build())
            .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if ((object.containsKey("message") && object.containsKey("name"))) {
            throw new AuthException(object.getString("name") + ", " + object.getString("message"));
        }
    }
}
