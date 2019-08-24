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
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.exception.AuthException;

import java.util.Map;

/**
 * Github登录
 *
 */
public class AuthGithubRequest extends AuthDefaultRequest {

    public AuthGithubRequest(AuthConfig config) {
        super(config, AuthSource.GITHUB);
    }

    public AuthGithubRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthSource.GITHUB, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        HttpResponse response = doPostAuthorizationCode(authCallback.getCode());
        Map<String, String> res = GlobalAuthUtil.parseStringToMap(response.body());
        if (res.containsKey("error")) {
            throw new AuthException(res.get("error") + ":" + res.get("error_description"));
        }
        return AuthToken.builder()
            .accessToken(res.get("access_token"))
            .scope(res.get("scope"))
            .tokenType(res.get("token_type"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpResponse response = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(response.body());
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
        return AuthUser.builder()
            .uuid(object.getString("id"))
            .username(object.getString("login"))
            .avatar(object.getString("avatar_url"))
            .blog(object.getString("blog"))
            .nickname(object.getString("name"))
            .company(object.getString("company"))
            .location(object.getString("location"))
            .email(object.getString("email"))
            .remark(object.getString("bio"))
            .gender(AuthUserGender.UNKNOWN)
            .token(authToken)
            .source(source)
            .build();
    }

}
