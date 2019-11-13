package com.geer2.nettyMqtt.server.queue;

import com.geer2.nettyMqtt.bean.SendMqttMessage;
import lombok.Data;

@Data
public class MessageEvent {

    private SendMqttMessage message;


}
