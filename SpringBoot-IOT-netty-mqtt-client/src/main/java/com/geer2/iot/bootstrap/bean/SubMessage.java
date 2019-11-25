package com.geer2.iot.bootstrap.bean;

import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * 订阅消息
 *
 * @author JiaweiWu
 * @create 2019-11-25 19:42
 **/
@Builder
@Data
public class SubMessage {

    private String topic;

    private MqttQoS qos;

}
