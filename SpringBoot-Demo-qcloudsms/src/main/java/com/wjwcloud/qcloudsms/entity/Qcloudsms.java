package com.wjwcloud.qcloudsms.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "qcloudsms")
public class Qcloudsms {

    // 短信应用SDK AppID
    // 1400开头
//    @Value(value = "${qcloudsms.appid}")
    private int appid;

    // 短信应用SDK AppKey
//    @Value(value = "${qcloudsms.appkey}")
    private   String appkey;

    // 短信模板ID，需要在短信应用中申请
    // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
//    @Value(value = "${qcloudsms.templateId}")
    public  int templateId;

    // 签名
    // NOTE: 真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
//    @Value(value = "${qcloudsms.smsSign}")
    private   String smsSign;

    // 需要发送短信的手机号码 {"21212313123", "12345678902", "12345678903"}
    private String[] phoneNumbers;

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getSmsSign() {
        return smsSign;
    }

    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign;
    }

    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
