package com.wjwcloud.qcloudsms.controller;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.wjwcloud.qcloudsms.SpringBootDemoQcloudsmsApplicationTests;
import com.wjwcloud.qcloudsms.entity.Qcloudsms;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class SmsController extends SpringBootDemoQcloudsmsApplicationTests{
    @Autowired
    private Qcloudsms qcloudsms;

    @Test
    public void oneTemp(){
        //单发模板短信
        try {
            String[] params = {"9","2","3","4","5","6","7","8","9"};
            SmsSingleSender ssender = new SmsSingleSender(qcloudsms.getAppid(), qcloudsms.getAppkey());
            String[] phoneNumbers = {"16620069844"};
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
                    qcloudsms.getTemplateId(), params, qcloudsms.getSmsSign(), "", "");
            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }
}
