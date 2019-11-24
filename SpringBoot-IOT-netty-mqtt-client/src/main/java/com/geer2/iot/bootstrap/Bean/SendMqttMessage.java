package com.geer2.iot.bootstrap.Bean;

import com.geer2.iot.enums.ConfirmStatus;
import lombok.Builder;
import lombok.Data;

/**
 * 消息
 *
 * @author JiaweiWu
 * @create 2019-11-04 19:36
 **/
@Data
@Builder
public class SendMqttMessage {

    private String Topic;

    private byte[] payload;

    private int qos;

    private boolean retained;

    private boolean dup;

    private int messageId;


    private long timestamp;

    private volatile ConfirmStatus confirmStatus;


}
