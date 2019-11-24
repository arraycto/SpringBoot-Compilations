package com.geer2.nettyprotocol.server.queue;

import com.geer2.nettyprotocol.server.bean.SendMqttMessage;
import lombok.Data;

@Data
public class MessageEvent {

    private SendMqttMessage message;


}
