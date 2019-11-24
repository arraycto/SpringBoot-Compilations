package com.geer2.nettyprotocol.server.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * 保留消息
 * @author
 * @create
 **/
@Builder
@Data
public class RetainMessage {

    private byte[]  byteBuf;

    private MqttQoS mqttQos;
    public String getString(){
        return new String(byteBuf);
    }
}
