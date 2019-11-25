package com.geer2.iot.bootstrap.cache;

import com.geer2.iot.bootstrap.bean.SendMqttMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存
 *
 * @author JiaweiWu
 * @create 2019-11-04 20:15
 **/
public class Cache {

    private static  ConcurrentHashMap<Integer,SendMqttMessage> message = new ConcurrentHashMap<>();


    public static  boolean put(Integer messageId,SendMqttMessage mqttMessage){

        return message.put(messageId,mqttMessage)==null;

    }

    public static SendMqttMessage get(Integer messageId){

        return  message.get(messageId);

    }


    public static SendMqttMessage del(Integer messageId){
        return message.remove(messageId);
    }
}
