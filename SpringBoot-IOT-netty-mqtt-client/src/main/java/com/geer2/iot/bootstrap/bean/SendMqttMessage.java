package com.geer2.iot.bootstrap.bean;

import com.geer2.iot.enums.ConfirmStatus;
import lombok.Builder;
import lombok.Data;

/**
 * 消息
 *
 * @author JiaweiWu
 * @create 2019-11-25 19:36
 **/
@Data
@Builder
public class SendMqttMessage {

    private String topic;

    private byte[] payload;

    private int qos;

    private boolean retained;

    private boolean dup;

    private int messageId;


    private long timestamp;

    private volatile ConfirmStatus confirmStatus;


}
