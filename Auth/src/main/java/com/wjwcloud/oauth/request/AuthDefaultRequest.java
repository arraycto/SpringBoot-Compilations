package com.wjwcloud.oauth.request;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.wjwcloud.oauth.cache.AuthStateCache;
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.exception.AuthException;
import com.wjwcloud.oauth.model.AuthCallback;
import com.wjwcloud.oauth.model.AuthToken;
import com.wjwcloud.oauth.model.AuthUser;
import com.wjwcloud.oauth.utils.AuthChecker;
import com.wjwcloud.oauth.utils.StringUtils;
import com.wjwcloud.oauth.utils.UrlBuilder;
import com.wjwcloud.oauth.utils.UuidUtils;
import com.wjwcloud.oauth.cache.AuthDefaultStateCache;
import com.wjwcloud.oauth.enums.AuthResponseStatus;
import com.wjwcloud.oauth.log.Log;
import com.wjwcloud.oauth.model.AuthResponse;

/**
 * 默认的request处理类
 */
public abstract class AuthDefaultRequest implements AuthRequest {
    protected AuthConfig config;
    protected AuthSource source;
    protected AuthStateCache authStateCache;

    public AuthDefaultRequest(AuthConfig config, AuthSource source) {
        this(config, source, AuthDefaultStateCache.INSTANCE);
    }

    public AuthDefaultRequest(AuthConfig config, AuthSource source, AuthStateCache authStateCache) {
        this.config = config;
        this.source = source;
        this.authStateCache = authStateCache;
        if (!AuthChecker.isSupportedAuth(config, source)) {
            throw new AuthException(AuthResponseStatus.PARAMETER_INCOMPLETE);
        }
        // 校验配置合法性
        AuthChecker.checkConfig(config, source);
    }

    /**
     * 获取access token
     *
     * @param authCallback 授权成功后的回调参数
     * @return token
     * @see AuthDefaultRequest#authorize()
     * @see AuthDefaultRequest#authorize(String)
     */
    protected abstract AuthToken getAccessToken(AuthCallback authCallback);

    /**
     * 使用token换取用户信息
     *
     * @param authToken token信息
     * @return 用户信息
     * @see AuthDefaultRequest#getAccessToken(AuthCallback)
     */
    protected abstract AuthUser getUserInfo(AuthToken authToken);

    /**
     * 统一的登录入口。当通过{@link AuthDefaultRequest#authorize(String)}授权成功后，会跳转到调用方的相关回调方法中
     * 方法的入参可以使用{@code AuthCallback}，{@code AuthCallback}类中封装好了OAuth2授权回调所需要的参数
     *
     * @param authCallback 用于接收回调参数的实体
     * @return AuthResponse
     */
    @Override
    public AuthResponse login(AuthCallback authCallback) {
        try {
            AuthChecker.checkCode(source, authCallback);
            this.checkState(authCallback.getState());

            AuthToken authToken = this.getAccessToken(authCallback);
            AuthUser user = this.getUserInfo(authToken);
            return AuthResponse.builder().code(AuthResponseStatus.SUCCESS.getCode()).data(user).build();
        } catch (Exception e) {
            Log.error("Failed to login with oauth authorization.", e);
            return this.responseError(e);
        }
    }

    /**
     * 处理{@link AuthDefaultRequest#login(AuthCallback)} 发生异常的情况，统一响应参数
     *
     * @param e 具体的异常
     * @return AuthResponse
     */
    private AuthResponse responseError(Exception e) {
        int errorCode = AuthResponseStatus.FAILURE.getCode();
        if (e instanceof AuthException) {
            errorCode = ((AuthException) e).getErrorCode();
        }
        return AuthResponse.builder().code(errorCode).msg(e.getMessage()).build();
    }

    /**
     * 返回授权url，可自行跳转页面
     * <p>
     * 不建议使用该方式获取授权地址，不带{@code state}的授权地址，容易受到csrf攻击。
     * 建议使用{@link AuthDefaultRequest#authorize(String)}方法生成授权地址，在回调方法中对{@code state}进行校验
     *
     * @return 返回授权地址
     * @see AuthDefaultRequest#authorize(String)
     */
    @Deprecated
    @Override
    public String authorize() {
        return this.authorize(null);
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
            .queryParam("state", getRealState(state))
            .build();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param code 授权码
     * @return 返回获取accessToken的url
     */
    protected String accessTokenUrl(String code) {
        return UrlBuilder.fromBaseUrl(source.accessToken())
            .queryParam("code", code)
            .queryParam("client_id", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("grant_type", "authorization_code")
            .queryParam("redirect_uri", config.getRedirectUri())
            .build();
    }

    /**
     * 返回获取accessToken的url
     *
     * @param refreshToken refreshToken
     * @return 返回获取accessToken的url
     */
    protected String refreshTokenUrl(String refreshToken) {
        return UrlBuilder.fromBaseUrl(source.refresh())
            .queryParam("client_id", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("refresh_token", refreshToken)
            .queryParam("grant_type", "refresh_token")
            .queryParam("redirect_uri", config.getRedirectUri())
            .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken token
     * @return 返回获取userInfo的url
     */
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    /**
     * 返回获取revoke authorization的url
     *
     * @param authToken token
     * @return 返回获取revoke authorization的url
     */
    protected String revokeUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.revoke()).queryParam("access_token", authToken.getAccessToken()).build();
    }

    /**
     * 获取state，如果为空， 则默认取当前日期的时间戳
     *
     * @param state 原始的state
     * @return 返回不为null的state
     */
    protected String getRealState(String state) {
        if (StringUtils.isEmpty(state)) {
            state = UuidUtils.getUUID();
        }
        // 缓存state
        authStateCache.cache(state, state);
        return state;
    }

    /**
     * 通用的 authorizationCode 协议
     *
     * @param code code码
     * @return HttpResponse
     */
    protected HttpResponse doPostAuthorizationCode(String code) {
        return HttpRequest.post(accessTokenUrl(code)).execute();
    }

    /**
     * 通用的 authorizationCode 协议
     *
     * @param code code码
     * @return HttpResponse
     */
    protected HttpResponse doGetAuthorizationCode(String code) {
        return HttpRequest.get(accessTokenUrl(code)).execute();
    }

    /**
     * 通用的 用户信息
     *
     * @param authToken token封装
     * @return HttpResponse
     */
    @Deprecated
    protected HttpResponse doPostUserInfo(AuthToken authToken) {
        return HttpRequest.post(userInfoUrl(authToken)).execute();
    }

    /**
     * 通用的 用户信息
     *
     * @param authToken token封装
     * @return HttpResponse
     */
    protected HttpResponse doGetUserInfo(AuthToken authToken) {
        return HttpRequest.get(userInfoUrl(authToken)).execute();
    }

    /**
     * 通用的post形式的取消授权方法
     *
     * @param authToken token封装
     * @return HttpResponse
     */
    @Deprecated
    protected HttpResponse doPostRevoke(AuthToken authToken) {
        return HttpRequest.post(revokeUrl(authToken)).execute();
    }

    /**
     * 通用的post形式的取消授权方法
     *
     * @param authToken token封装
     * @return HttpResponse
     */
    protected HttpResponse doGetRevoke(AuthToken authToken) {
        return HttpRequest.get(revokeUrl(authToken)).execute();
    }


    /**
     * 校验回调传回的state
     *
     * @param state {@code state}一定不为空
     */
    protected void checkState(String state) {
        if (StringUtils.isEmpty(state) || !authStateCache.containsKey(state)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_REQUEST);
        }
    }
}
