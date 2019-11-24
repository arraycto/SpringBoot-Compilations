package com.geer2.iot.bootstrap.api;


import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author JiaweiWu
 */
public abstract class AbstractClientMqttHandlerService implements MqttHandlerIntf{

    @Override
    public void doTimeOut(Channel channel, IdleStateEvent evt) {
        heart(channel,evt);
    }

    public abstract void  heart(Channel channel, IdleStateEvent evt);

    public abstract void suback(Channel channel, MqttSubAckMessage mqttMessage) ;

    public abstract void pubBackMessage(Channel channel, int i);

    public abstract void unsubBack(Channel channel, MqttMessage mqttMessage);
}
