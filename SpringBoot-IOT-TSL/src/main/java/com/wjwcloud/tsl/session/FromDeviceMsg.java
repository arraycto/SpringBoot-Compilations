package com.wjwcloud.tsl.session;

import java.io.Serializable;

/**
 * @Author:
 * @Date: 19-4-2
 * @Version 1.0
 */
public interface FromDeviceMsg extends Serializable {

    SessionMsgType getMsgType();

}
