package com.wjwcloud.iot.voicecontrol.aligenie.enums;


/**
 * 产品类型
 */
public enum ProductTypeEnum {
    //未知
    UN_KNOWN("unKnown", "未知"),
    //智能锁
    LOCK("lock", "智能锁"),
    //猫眼
    PEEPHOLE("peephole", "猫眼"),
    //NB
    NB_IOT("NB", "NB"),
    //智能锁
    FAN("fan", "风扇")
    ;

    String code;  // 类型编码
    String name; // 类型名称


    ProductTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code 获取
     *
     * @param code
     * @return
     */
    public static ProductTypeEnum getProductType(String code) {
        for (ProductTypeEnum productType : values()) {
            if (productType.code.equals(code)) {
                return productType;
            }
        }
        return UN_KNOWN;
    }


}
