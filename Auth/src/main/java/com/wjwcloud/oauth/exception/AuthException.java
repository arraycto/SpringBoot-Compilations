package com.wjwcloud.oauth.exception;

import com.wjwcloud.oauth.enums.AuthResponseStatus;

/**
 * JustAuth通用异常类
 *
 */
public class AuthException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public AuthException(String errorMsg) {
        this(AuthResponseStatus.FAILURE.getCode(), errorMsg);
    }

    public AuthException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AuthException(AuthResponseStatus status) {
        super(status.getMsg());
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
