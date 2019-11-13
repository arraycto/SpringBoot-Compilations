package com.wjwcloud.tsl.core;

import com.wjwcloud.tsl.data.kv.AttributeKvEntry;
import com.wjwcloud.tsl.session.FromDeviceRequestMsg;

import java.util.Set;

/**
 * @Author:
 * @Date: 19-4-2
 * @Version 1.0
 */
public interface AttributesUpdateRequest extends FromDeviceRequestMsg {

    Set<AttributeKvEntry> getAttributes();

}
