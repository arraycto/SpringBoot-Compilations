
package com.wjwcloud.ad.core.domain;

import org.omg.CORBA.ACTIVITY_COMPLETED;

/**
 * FunctionCodeEnum
 * 派生子类类型
 * @author JiaweiWu
 */
public enum FunctionCodeEnum {
    /**
     * 新人有礼活动
     */
    ACTIVITY_PARTICIPATE("participate"),
    ACTIVITY_RELEASE("release"),
    /**
     * 设备减免活动
     */
    ACTIVITY_REDUCTION("reduction");

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
