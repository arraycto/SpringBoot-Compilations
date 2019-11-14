package com.geer2.nettyMqtt.server.api;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 自定义 对外暴露 消息处理api
 *
 * @author JiaweiWu
 * @create
 **/
public interface MqttHandlerIntf {

    void close(Channel channel);

    void puback(Channel channel, MqttMessage mqttMessage);

    void pubrec(Channel channel, MqttMessage mqttMessage);

    void pubrel(Channel channel, MqttMessage mqttMessage);

    void pubcomp(Channel channel, MqttMessage mqttMessage);

    void doTimeOut(Channel channel, IdleStateEvent evt);

    void doTimeOutEvt(Channel channel, IdleStateEvent evt);


}
