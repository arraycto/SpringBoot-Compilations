package com.geer2.nettyMqtt.server.sendMessage;

import com.geer2.nettyMqtt.bean.DeviceManage;
import com.geer2.nettyMqtt.bean.forBusiness.MsgToNode;
import com.geer2.nettyMqtt.bean.forBusiness.UpMessage;
import com.geer2.nettyMqtt.server.channel.MqttHandlerServerImpl;
import com.geer2.nettyMqtt.server.handler.HttpServerHandler;
import com.geer2.nettyMqtt.util.Constants;
import com.geer2.nettyMqtt.util.json.gson.GsonJsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 离线用户消息下发
 * @author JiaweiWu
 * @Date
 */
public class SendOfflineMessageThread extends Thread
{
    public static Logger log = LogManager.getLogger(SendOfflineMessageThread.class);
    private String stb_code;
    
    private MsgToNode msg;
    
    public SendOfflineMessageThread(MsgToNode msg, String stbCode)
    {
        this.stb_code = stbCode;
        this.msg = msg;
    }
    
    @Override
    public void run()
    {
        log.debug("离线用户消息下发线程 start，用户为："+stb_code+" 消息编码："+msg.getMsgInfo().getMsgCode());
        
        /**
         * 离线用户下发消息，并写入上报文件。从离线消息缓存，这条消息的离线集合中删除该用户
         */
        if (DeviceManage.DEVICE_MAP.containsKey(stb_code))
        {
            ChannelHandlerContext ctxx = (ChannelHandlerContext) DeviceManage.DEVICE_MAP.get(stb_code);
            if (ctxx != null && ctxx.channel().isActive())
            {
                MqttPublishMessage pubMsg;
                pubMsg = MqttHandlerServerImpl.buildPublish(GsonJsonUtil.toJson(msg.getMsgInfo()), Constants.TOPIC_STB, 1);
                ReferenceCountUtil.retain(pubMsg);
                ctxx.writeAndFlush(pubMsg);
                UpMessage upmessage=new UpMessage();
                upmessage.setDeviceId(stb_code);
                upmessage.setMsgCode(msg.getMsgInfo().getMsgCode());
                upmessage.setStatus(Constants.SENDSUCCESS);
                upmessage.setDate(UpMessage.getCurrentDate());
                upmessage.setMsgType(msg.getMsgInfo().getMsgType());
                if(HttpServerHandler.messageMap.containsKey(msg.getMsgInfo().getMsgCode()))
                {
                    upmessage.setUserNums(HttpServerHandler.messageMap.get(msg.getMsgInfo().getMsgCode()).getUserNumbers());
                }
                HttpServerHandler.reportMsgLog.debug(upmessage.getDeviceId()+"||"+upmessage.getMsgCode()+"||"
                        +upmessage.getStatus()+"||"+upmessage.getIsOnclick()+"||"+upmessage.getDate()
                        +"||"+upmessage.getUserNums()+"||"+upmessage.getMsgType());
                HttpServerHandler.OffLineUserMsgMap.get(msg.getMsgInfo().getMsgCode()).remove(stb_code);
                log.debug("离线用户消息下发线程 end 消息发送成功，用户为："+stb_code+" 剩余离线用户数为："+ HttpServerHandler.OffLineUserMsgMap.get(msg.getMsgInfo().getMsgCode()).size()+" 消息编码："+msg.getMsgInfo().getMsgCode()+" 下发消息内容："+GsonJsonUtil.toJson(msg.getMsgInfo()));
            }else
            {
                log.debug("离线用户消息下发线程 end，用户未上线（已断开）。用户为："+stb_code+" 消息编码："+msg.getMsgInfo().getMsgCode());
            }
        }else
        {
            log.debug("离线用户消息下发线程 end ，用户未上线（已断开）。用户为："+stb_code+" 消息编码："+msg.getMsgInfo().getMsgCode());
        }
    }
    
}
