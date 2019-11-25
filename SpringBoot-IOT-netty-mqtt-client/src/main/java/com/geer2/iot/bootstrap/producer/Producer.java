package com.geer2.iot.bootstrap.producer;

import com.geer2.iot.auto.MqttListener;
import com.geer2.iot.bootstrap.bean.SubMessage;
import com.geer2.iot.commons.properties.ConnectOptions;
import io.netty.channel.Channel;

import java.util.List;

/**
 * 生产者
 *
 * @author JiaweiWu
 * @create 2019-11-25 17:17
 **/
public interface Producer {

    /**
     * 获取Channel
     * @return
     */
    Channel getChannel();

    /**
     * 连接服务端
     * @param connectOptions
     * @return
     */
    Producer connect(ConnectOptions connectOptions);

    /**
     * 断开连接
     */
    void  close();

    /**
     * 设置Mqtt监听
     * @param mqttListener
     */
    void setMqttListener(MqttListener mqttListener);

    /**
     * 消息推送
     * @param topic
     * @param message
     * @param retained
     * @param qos
     */
    void pub(String topic,String message,boolean retained,int qos);

    /**
     *消息推送
     * @param topic
     * @param message
     */
    void pub(String topic,String message);

    /**
     *消息推送
     * @param topic
     * @param message
     * @param qos
     */
    void pub(String topic,String message,int qos);

    /**
     *消息推送
     * @param topic
     * @param message
     * @param retained
     */
    void pub(String topic,String message,boolean retained);

    /**
     * 主题订阅
     * @param subMessages
     */
    void sub(SubMessage... subMessages);

    /**
     * 取消订阅
     * @param topics
     */
    void unsub(List<String> topics);

    /**
     * 取消所有订阅
     */
    void unsub();

    /**
     * 断开连接
     */
    void disConnect();

}
