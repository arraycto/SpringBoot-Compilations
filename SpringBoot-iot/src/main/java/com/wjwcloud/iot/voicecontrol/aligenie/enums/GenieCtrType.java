package com.wjwcloud.iot.voicecontrol.aligenie.enums;

/**
 * 设备发现、操作的状态
 * Created by zhoulei on 2019/4/826
 */
public enum GenieCtrType {

//    设备发现("AliGenie.Iot.Device.Discovery"),
//    设备控制("AliGenie.Iot.Device.Control"),
//    设备属性查询("AliGenie.Iot.Device.Query");
    DEVICE_DISCOVERY("AliGenie.Iot.Device.Discovery"),
    DEVICE_CONTROL("AliGenie.Iot.Device.Control"),
    DEVICE_QUERY("AliGenie.Iot.Device.Query");
    private final String value;
    GenieCtrType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
