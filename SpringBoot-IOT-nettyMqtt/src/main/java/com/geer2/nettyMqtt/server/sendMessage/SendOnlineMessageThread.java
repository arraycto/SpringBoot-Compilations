package com.geer2.nettyMqtt.server.sendMessage;

import com.geer2.nettyMqtt.bean.forBusiness.MsgPublish;
import com.geer2.nettyMqtt.bean.forBusiness.MsgToNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * 在线用户下发指令线程
 * @author JiaweiWu
 *
 *
 */
public class SendOnlineMessageThread extends Thread
{
    public static Logger log = LogManager.getLogger(SendOnlineMessageThread.class);
    MsgToNode msg;
    public SendOnlineMessageThread(MsgToNode message)
    {
        this.msg = message;
    }
    
    @Override
    public void run()
    {
       log.debug("指令下发线程 start，消息编码："+msg.getMsgInfo().getMsgCode());
        MsgPublish mpg = msg.getMsgPublish();
        Set<String> userSet = null;
        /**
         * 从数据库查询下发用户集合
         */
//        UserDao userdao = new UserDao();
//        userSet = userdao.getUsers(mpg.getMsgPushType(), mpg.getMsgPushDst());
//        //应下发用户总数
//        int usernums=0;
//        //实际下发用户数
//        int offusernums=0;
//        if (userSet != null && userSet.size() > 0)
//        {
//            usernums=userSet.size();
//            log.debug("指令下发线程，应下发用户总数："+userSet.size()+". 消息编码："+msg.getMsgInfo().getMsgCode());
//            msg.setUserNumbers(userSet.size());
//            msg.setEditTime(new Date());
//            HttpServerHandler.messageMap.put(msg.getMsgInfo().getMsgCode(), msg);
//            Iterator<String> it = userSet.iterator();
//            while (it.hasNext())
//            {
//                String stb_code = it.next();
//                /**
//                 * 在线用户下发消息，并写入上报文件。从待发送用户集合删除已发送用户
//                 */
//                if (MqttServerHandler.userMap.containsKey(stb_code))
//                {
//                    ChannelHandlerContext ctxx = (ChannelHandlerContext)MqttServerHandler.userMap.get(stb_code);
//                    if (ctxx != null && ctxx.channel().isActive())
//                    {
//                        MqttPublishMessage pubMsg;
//                        UpMessage upmessage=new UpMessage();
//                        upmessage.setDeviceId(stb_code);
//                        upmessage.setMsgCode(msg.getMsgInfo().getMsgCode());
//                        upmessage.setStatus(Constants.SENDSUCCESS);
//                        upmessage.setDate(UpMessage.getCurrentDate());
//                        upmessage.setUserNums(usernums);
//                        upmessage.setMsgType(msg.getMsgInfo().getMsgType());
//                        pubMsg = MqttServerHandler.buildPublish(GsonJsonUtil.toJson(msg.getMsgInfo()), Constants.TOPIC_STB, 222);
////                        ReferenceCountUtil.retain(pubMsg);
//                        ctxx.writeAndFlush(pubMsg);
//                        //消息状态为6停播消息的时候不记录日志
//                        if(msg.getMsgInfo().getStatus() != 6){
//                            HttpServerHandler.reportMsgLog.debug(upmessage.getDeviceId()+"||"+upmessage.getMsgCode()+"||"
//                                    +upmessage.getStatus()+"||"+upmessage.getIsOnclick()+"||"+upmessage.getDate()
//                                    +"||"+upmessage.getUserNums()+"||"+upmessage.getMsgType());
//                        }
//                        log.debug("指令下发线程，下发消息成功，下发用户："+stb_code+". 消息编码："+msg.getMsgInfo().getMsgCode()+" 下发消息内容："+GsonJsonUtil.toJson(msg.getMsgInfo()));
//                        it.remove();
//                    }
//                }
//            }
//            /**
//             * 有用户没有下发，统一当离线用户处理
//             */
//            if (userSet != null & userSet.size() > 0)
//            {
//                offusernums=userSet.size();
//                if (HttpServerHandler.OffLineUserMsgMap.containsKey(msg.getMsgInfo().getMsgCode()))
//                {
//                    Set<String> oldusers = HttpServerHandler.OffLineUserMsgMap.get(msg.getMsgInfo().getMsgCode());
//                    userSet.addAll(oldusers);
//                }
////                log.debug("指令下发线程，下发消息完毕，剩余离线用户数:"+userSet.size()+". 消息编码："+msg.getMsgInfo().getMsgCode());
//                HttpServerHandler.OffLineUserMsgMap.put(msg.getMsgInfo().getMsgCode(), userSet);
//            }
//        }
        
//        log.debug("指令下发线程  end,应下发用户总数："+usernums+",实际下发用户数："+(usernums-offusernums)+"。下发消息："+GsonJsonUtil.toJson(msg.getMsgInfo()));
        
    }
    
    public void sendMessage(String code, int msgCode, int op, String planId, String str, int requestId)
    {
        
    }
    
}
