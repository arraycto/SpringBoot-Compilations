package com.geer2.nettyMqtt.bean;

import com.geer2.nettyMqtt.server.constant.enums.ConfirmStatus;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * mqtt 消息
 *
 * @author
 * @create
 **/
@Builder
@Data
public class SendMqttMessage {

    private int messageId;

    private Channel channel;

    private volatile ConfirmStatus confirmStatus;

    private long time;

    private byte[]  byteBuf;

    private boolean isRetain;

    private MqttQoS qos;

    private String topic;

}
