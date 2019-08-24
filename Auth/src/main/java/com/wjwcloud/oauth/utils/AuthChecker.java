package com.wjwcloud.oauth.utils;

import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.model.AuthCallback;
import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.enums.AuthResponseStatus;
import com.wjwcloud.oauth.exception.AuthException;

/**
 * 授权配置类的校验器
 */
public class AuthChecker {

    /**
     * 是否支持第三方登录
     *
     * @param config config
     * @param source source
     * @return true or false
     * @since 1.6.1-beta
     */
    public static boolean isSupportedAuth(AuthConfig config, AuthSource source) {
        boolean isSupported = StringUtils.isNotEmpty(config.getClientId()) && StringUtils.isNotEmpty(config.getClientSecret()) && StringUtils.isNotEmpty(config.getRedirectUri());
        if (isSupported && AuthSource.ALIPAY == source) {
            isSupported = StringUtils.isNotEmpty(config.getAlipayPublicKey());
        }
        if (isSupported && AuthSource.STACK_OVERFLOW == source) {
            isSupported = StringUtils.isNotEmpty(config.getStackOverflowKey());
        }
        if (isSupported && AuthSource.WECHAT_ENTERPRISE == source){
            isSupported = StringUtils.isNotEmpty(config.getAgentId());
        }
        return isSupported;
    }

    /**
     * 检查配置合法性。针对部分平台， 对redirect uri有特定要求。一般来说redirect uri都是http://，而对于facebook平台， redirect uri 必须是https的链接
     *
     * @param config config
     * @param source source
     * @since 1.6.1-beta
     */
    public static void checkConfig(AuthConfig config, AuthSource source) {
        String redirectUri = config.getRedirectUri();
        if (!GlobalAuthUtil.isHttpProtocol(redirectUri) && !GlobalAuthUtil.isHttpsProtocol(redirectUri)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_REDIRECT_URI);
        }
        // facebook的回调地址必须为https的链接
        if (AuthSource.FACEBOOK == source && !GlobalAuthUtil.isHttpsProtocol(redirectUri)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_REDIRECT_URI);
        }
        // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1
        if (AuthSource.ALIPAY == source && GlobalAuthUtil.isLocalHost(redirectUri)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_REDIRECT_URI);
        }
    }

    /**
     * 校验回调传回的code
     * <p>
     * {@code v1.10.0}版本中改为传入{@code source}和{@code callback}，对于不同平台使用不同参数接受code的情况统一做处理
     *
     * @param source   当前授权平台
     * @param callback 从第三方授权回调回来时传入的参数集合
     * @since 1.8.0
     */
    public static void checkCode(AuthSource source, AuthCallback callback) {
        String code = callback.getCode();
        if (source == AuthSource.ALIPAY) {
            code = callback.getAuth_code();
        } else if (source == AuthSource.HUAWEI) {
            code = callback.getAuthorization_code();
        }
        if (StringUtils.isEmpty(code)) {
            throw new AuthException(AuthResponseStatus.ILLEGAL_CODE);
        }
    }
}
