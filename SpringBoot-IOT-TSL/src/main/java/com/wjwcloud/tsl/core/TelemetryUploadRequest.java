package com.wjwcloud.tsl.core;

import com.wjwcloud.tsl.data.kv.KvEntry;
import com.wjwcloud.tsl.session.FromDeviceRequestMsg;

import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Date: 19-4-2
 * @Version 1.0
 */
public interface TelemetryUploadRequest extends FromDeviceRequestMsg {

    Map<Long, List<KvEntry>> getData();
}
