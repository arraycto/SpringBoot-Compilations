package com.geer2.iot.auto;

/**
 * call scan
 * @author JiaweiWu
 * @create 2019-11-04 18:42
 **/
public interface MqttListener{

    void callBack(String topic,String msg);

    void callThrowable(Throwable e);
}
