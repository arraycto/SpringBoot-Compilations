package com.geer2.nettyMqtt.server.channel;

import com.geer2.nettyMqtt.bean.WillMeaasge;
import com.geer2.nettyMqtt.server.api.BaseApi;
import com.geer2.nettyMqtt.server.api.ChannelService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 遗嘱消息处理
 *
 * @author
 * @create
 **/
@Slf4j
@Component
@Setter
@Getter
@NoArgsConstructor
public class WillService implements BaseApi {


    @Autowired
    ChannelService channelService;

    // deviceid -WillMeaasge
    private static  ConcurrentHashMap<String, WillMeaasge> willMeaasges = new ConcurrentHashMap<>();



    /**
     * 保存遗嘱消息
     */
    public void save(String deviceid, WillMeaasge build) {
        // 替换旧的
        willMeaasges.put(deviceid,build);
    }

    // 客户端断开连接后 开启遗嘱消息发送
    public void doSend( String deviceId) {
        if(StringUtils.isNotBlank(deviceId)&&(willMeaasges.get(deviceId))!=null){
            WillMeaasge willMeaasge = willMeaasges.get(deviceId);
            // 发送遗嘱消息
            channelService.sendWillMsg(willMeaasge);
            // 移除
            if(!willMeaasge.isRetain()){
                willMeaasges.remove(deviceId);
                log.info("deviceId will message["+willMeaasge.getWillMessage()+"] is removed");
            }
        }
    }

    /**
     * 删除遗嘱消息
     */
    public void del(String deviceid ) {willMeaasges.remove(deviceid);}
}
