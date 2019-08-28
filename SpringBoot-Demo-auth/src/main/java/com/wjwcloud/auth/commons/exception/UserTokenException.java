package com.wjwcloud.auth.commons.exception;


import com.wjwcloud.auth.commons.constant.CommonConstants;

/**
 * 用户Token异常
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
