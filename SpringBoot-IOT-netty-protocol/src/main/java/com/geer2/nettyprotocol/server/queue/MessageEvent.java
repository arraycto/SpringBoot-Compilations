package com.geer2.nettyprotocol.server.queue;

import com.geer2.nettyprotocol.server.bean.SendMqttMessage;
import lombok.Data;
/**
 * @author JiaweiWu
 */
@Data
public class MessageEvent {

    private SendMqttMessage message;


}
