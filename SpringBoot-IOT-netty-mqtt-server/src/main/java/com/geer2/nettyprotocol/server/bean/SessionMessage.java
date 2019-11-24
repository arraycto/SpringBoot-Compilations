package com.geer2.nettyprotocol.server.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * Session会话数据保存
 *
 * @author
 * @create
 **/
@Builder
@Data
public class SessionMessage {

    private byte[]  byteBuf;

    private MqttQoS mqttQos;

    private  String topic;


    public String getString(){
        return new String(byteBuf);
    }
}
