package com.geer2.nettyprotocol.server.mqtt.constant.enums;/**
 * Created by wangcy on 2017/12/14.
 */

/**
 * Qos确认状态
 *
 * @author JiaweiWu
 **/
public enum QosStatus {

    /**
     *  已发送 没收到RECD （发送）
     */
    PUBD,

    /**
     * publish 推送回复过（发送）
     */
    RECD,


}
