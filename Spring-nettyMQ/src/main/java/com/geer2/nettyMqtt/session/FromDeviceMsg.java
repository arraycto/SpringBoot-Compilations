package com.geer2.nettyMqtt.session;

import java.io.Serializable;

/**
 * @Author:
 * @Date:
 * @Version 1.0
 */
public interface FromDeviceMsg extends Serializable {

    SessionMsgType getMsgType();

}
