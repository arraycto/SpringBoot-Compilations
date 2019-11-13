package com.geer2.nettyMqtt.core;

import com.geer2.nettyMqtt.session.FromDeviceRequestMsg;
import com.geer2.nettyMqtt.util.data.kv.AttributeKvEntry;

import java.util.Set;

/**
 * @Author:
 * @Date: 19-4-2
 * @Version 1.0
 */
public interface AttributesUpdateRequest extends FromDeviceRequestMsg {

    Set<AttributeKvEntry> getAttributes();

}
