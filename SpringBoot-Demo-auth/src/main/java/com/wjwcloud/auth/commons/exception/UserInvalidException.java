package com.wjwcloud.auth.commons.exception;


import com.wjwcloud.auth.commons.constant.CommonConstants;

/**
 * 用户不存在或账户密码错误 异常
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}
