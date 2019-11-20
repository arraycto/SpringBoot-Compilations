package com.geer2.nettyprotocol.server.mqtt.channel;

import com.geer2.nettyprotocol.server.bean.SessionMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 会话保留处理
 *
 * @author
 * @create
 **/
@Service
public class ClientSessionService {

    /**
     * 连接关闭后 保留此session 数据  deviceId
     */
    private static ConcurrentHashMap<String,ConcurrentLinkedQueue<SessionMessage>>  queueSession  = new ConcurrentHashMap<>();


    public  void saveSessionMsg(String deviceId, SessionMessage sessionMessage) {
        ConcurrentLinkedQueue<SessionMessage> sessionMessages = queueSession.getOrDefault(deviceId, new ConcurrentLinkedQueue<>());
        boolean flag;
        do{
             flag = sessionMessages.add(sessionMessage);
        }
        while (!flag);
        queueSession.put(deviceId,sessionMessages);
    }

    public  ConcurrentLinkedQueue<SessionMessage> getByteBuf(String deviceId){
        return   Optional.ofNullable(deviceId).map(s -> queueSession.get(s))
                .orElse(null);
    }
}
