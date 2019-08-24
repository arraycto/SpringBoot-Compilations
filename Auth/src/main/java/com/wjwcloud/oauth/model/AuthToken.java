package com.wjwcloud.oauth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 授权所需的token
 */
@Getter
@Setter
@Builder
public class AuthToken {
    private String accessToken;
    private int expireIn;
    private String refreshToken;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;

    /**
     * Google附带属性
     */
    private String scope;
    private String tokenType;
    private String idToken;

    /**
     * 小米附带属性
     */
    private String macAlgorithm;
    private String macKey;

    /**
     * 企业微信附带属性
     *
     * @since 1.10.0
     */
    private String code;

}
