package com.geer2.nettyMqtt.bean;

import lombok.Builder;
import lombok.Data;

/**
 * 遗嘱消息
 *
 * @author
 * @create
 **/
@Builder
@Data
public class WillMeaasge {

    private String willTopic;

    private String willMessage;


    private  boolean isRetain;

    private int qos;

}
