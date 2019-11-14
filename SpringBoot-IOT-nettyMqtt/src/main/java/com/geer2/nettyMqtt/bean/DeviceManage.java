package com.geer2.nettyMqtt.bean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JiaweiWu
 */
public class DeviceManage {

    public static final AttributeKey<String> DEVICE = AttributeKey.valueOf("deviceId");

    /**
     * 断开连接的设备
     */
    public static Map<String,Long> UN_CONNECT_MAP=new HashMap<String, Long>();
    /**
     * 设备数据缓存。<设备编号，ctx>
     */
    public static Map<String, ChannelHandlerContext> DEVICE_MAP = new ConcurrentHashMap<String, ChannelHandlerContext>();

    /**
     * 记载在线用户登入时间
     */
    public static Map<String, String> DEVICE_ONLINE_MAP = new ConcurrentHashMap<String, String>();

    public static Map<String, ChannelHandlerContext> getDeviceMap()
    {
        return DEVICE_MAP;
    }

    public static void setDeviceMap(Map<String, ChannelHandlerContext> deviceMap)
    {
        DeviceManage.DEVICE_MAP = deviceMap;
    }
}
