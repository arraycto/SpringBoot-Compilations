package com.geer2.iot.bootstrap.api;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 自定义 对外暴露 消息处理api
 *
 * @author JiaweiWu
 * @create 2019-11-21 9:53
 **/
public interface MqttHandlerIntf {

    /**
     * 关闭
     * @param channel
     */
    void close(Channel channel);

    /**
     * 消息回复确认(qos1 级别 保证收到消息  但是可能会重复)
     *  PUBACK报文是对QoS 1等级的PUBLISH报文的响应。
     * @param channel
     * @param mqttMessage
     */
    void puback(Channel channel, MqttMessage mqttMessage);

    /**
     * qos2 发布收到
     * ( QoS 2， 第一步）：
     *  PUBREC报文是对QoS等级2的PUBLISH报文的响应。 它是QoS 2等级协议交换的第二个报文。
     * @param channel
     * @param mqttMessage
     */
    void pubrec(Channel channel, MqttMessage mqttMessage);

    /**
     * qos2 发布释放
     * （ QoS 2， 第二步）：
     *      PUBREL报文是对PUBREC报文的响应。 它是QoS 2等级协议交换的第三个报文。
     * @param channel
     * @param mqttMessage
     */
    void pubrel(Channel channel, MqttMessage mqttMessage);

    /**
     * qos2 发布完成
     * （ QoS 2， 第三步）：
     *      PUBCOMP报文是对PUBREL报文的响应。 它是QoS 2等级协议交换的第四个也是最后一个报文。
     * @param channel
     * @param mqttMessage
     */
    void pubcomp(Channel channel, MqttMessage mqttMessage);

    /**
     * 超时
     * @param channel
     * @param evt
     */
    void doTimeOut(Channel channel, IdleStateEvent evt);


}
