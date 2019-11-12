package com.geer2.nettyMqtt.core;

import com.geer2.nettyMqtt.session.FromDeviceRequestMsg;
import com.geer2.nettyMqtt.util.data.kv.KvEntry;

import java.util.List;
import java.util.Map;

/**
 * @Author: 穆书伟
 * @Date: 19-4-2
 * @Version 1.0
 */
public interface TelemetryUploadRequest extends FromDeviceRequestMsg {

    Map<Long, List<KvEntry>> getData();
}
