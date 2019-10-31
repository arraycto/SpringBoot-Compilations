
package com.wjwcloud.ad.core.domain;

/**
 * FunctionCodeEnum
 * 派生子类类型
 * @author JiaweiWu
 */
public enum FunctionCodeEnum {
    ACTIVITY_PARTICIPATE("participate"),
    ACTIVITY_RELEASE("release");

    private String code;

    FunctionCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
