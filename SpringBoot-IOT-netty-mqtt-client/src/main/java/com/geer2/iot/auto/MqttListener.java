package com.geer2.iot.auto;

/**
 * call scan
 * @author JiaweiWu
 * @create 2019-11-04 18:42
 **/
public interface MqttListener{

    /**
     * 订阅主题获取消息
     * @param topic
     * @param msg
     */
    void callBack(String topic,String msg);

    /**
     * 订阅监听  异常处理
     * @param e
     */
    void callThrowable(Throwable e);
}
