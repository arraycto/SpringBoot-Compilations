
package com.wjwcloud.ad.system.exception;

/**
 * BusinessRuntimeException
 */
public class BusinessRuntimeException extends RuntimeException {
    /**
     * the code
     */
    private String code;
    /**
     * the msg
     */
    private String msg;

    public BusinessRuntimeException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
