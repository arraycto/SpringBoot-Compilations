package com.geer2.nettyMqtt.server.channel;

import com.geer2.nettyMqtt.bean.DeviceManage;
import com.geer2.nettyMqtt.bean.forBusiness.UpMessage;
import com.geer2.nettyMqtt.bean.forStb.StbReportMsg;
import com.geer2.nettyMqtt.server.api.ChannelService;
import com.geer2.nettyMqtt.server.api.MqttHandlerService;
import com.geer2.nettyMqtt.server.handler.HttpServerHandler;
import com.geer2.nettyMqtt.server.utils.MqttTopicMatcher;
import com.geer2.nettyMqtt.util.Constants;
import com.geer2.nettyMqtt.util.json.gson.GsonJsonUtil;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static io.netty.handler.codec.mqtt.MqttQoS.AT_LEAST_ONCE;

/**
 * @author JiaweiWu
 */
@Component
public class MqttHandlerServerImpl extends MqttHandlerService {

    @Autowired
    ChannelService mqttChannelService;



    public static Logger log = LogManager.getLogger(MqttHandlerServerImpl.class);

    public static final MqttQoS MAX_SUPPORTED_QOS_LVL = MqttQoS.AT_LEAST_ONCE;

    private final ConcurrentMap<MqttTopicMatcher,Integer> mqttQoSMap = new ConcurrentHashMap<>();

    /**
     *  客户端发布消息
     * @param channel
     * @param mqttPublishMessage
     */
    @Override
    public void publish(Channel channel, MqttPublishMessage mqttPublishMessage) {
        ByteBuf buf = mqttPublishMessage.payload();
        String msg = new String(ByteBufUtil.getBytes(buf));
        log.debug("终端消息上报 start，终端编码为："+channel.attr(DeviceManage.DEVICE).get()+" 终端上报消息体："+msg);
        int msgId = mqttPublishMessage.variableHeader().messageId();
        if (msgId == -1) {
            msgId = 1;
        }
        //主题名
        String topicName = mqttPublishMessage.variableHeader().topicName();
        System.out.println("上报消息的主题："+topicName);
        try
        {
            //上报消息写入
            StbReportMsg stbmsg= GsonJsonUtil.fromJson(msg, StbReportMsg.class);
            //机顶盒编号||消息编号||发送状态||点击状态 ||更新时间||消息应下发用户总数
            if(!StringUtils.isEmpty(stbmsg.getMsgId()))
            {
                UpMessage upmessage=new UpMessage();
                upmessage.setDeviceId(StringUtils.isEmpty(stbmsg.getDeviceNum())?channel.attr(DeviceManage.DEVICE).get():stbmsg.getDeviceNum());
                upmessage.setMsgCode(stbmsg.getMsgId());
                upmessage.setStatus(stbmsg.getStatus());
                upmessage.setIsOnclick(stbmsg.getJumpFlag());
                upmessage.setDate(UpMessage.getCurrentDate());
                upmessage.setMsgType(stbmsg.getMsgType());
                if(HttpServerHandler.messageMap.containsKey(stbmsg.getMsgId()))
                {
                    upmessage.setUserNums(HttpServerHandler.messageMap.get(stbmsg.getMsgId()).getUserNumbers());
                }
                log.debug("终端消息上报 end 终端上报消息成功。终端编号："+channel.attr(DeviceManage.DEVICE).get()+" 消息编码："+stbmsg.getMsgId()+"消息状态："+stbmsg.getStatus());
                HttpServerHandler.reportMsgLog.debug(upmessage.getDeviceId()+"||"+upmessage.getMsgCode()+"||"
                        +upmessage.getStatus()+"||"+upmessage.getIsOnclick()+"||"+upmessage.getDate()
                        +"||"+upmessage.getUserNums()+"||"+upmessage.getMsgType());
            }else
            {
                log.error("终端消息上报 end 终端上报消息编码为空！终端编号为: "+ channel.attr(DeviceManage.DEVICE).get()+" 上报消息为： "+msg);
            }
        }
        catch (JsonSyntaxException e)
        {
            log.error("终端消息上报 end 终端上报消息格式错误！终端编号为: "+channel.attr(DeviceManage.DEVICE).get()+" 上报消息为： "+msg);
        }

        if (mqttPublishMessage.fixedHeader().qosLevel() == AT_LEAST_ONCE)
        {
            MqttMessageIdVariableHeader header = MqttMessageIdVariableHeader.from(msgId);
            MqttPubAckMessage puback = new MqttPubAckMessage(Constants.PUBACK_HEADER, header);
            channel.write(puback);
        }
        msg = null;
        topicName = null;
    }

    /**
     * 订阅
     * @param channel
     * @param mqttSubscribeMessage
     */
    @Override
    public void subscribe(Channel channel, MqttSubscribeMessage mqttSubscribeMessage) {
        Set<String> topics = mqttSubscribeMessage.payload().topicSubscriptions().stream().map(mqttTopicSubscription ->
                mqttTopicSubscription.topicName()
        ).collect(Collectors.toSet());
        mqttChannelService.suscribeSuccess(mqttChannelService.getDeviceId(channel), topics);
        subBack(channel, mqttSubscribeMessage, topics.size());
    }
    private void subBack(Channel channel, MqttSubscribeMessage mqttSubscribeMessage, int num) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(mqttSubscribeMessage.variableHeader().messageId());
        List<Integer> grantedQoSLevels = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            grantedQoSLevels.add(mqttSubscribeMessage.payload().topicSubscriptions().get(i).qualityOfService().value());
        }
        MqttSubAckPayload payload = new MqttSubAckPayload(grantedQoSLevels);
        MqttSubAckMessage mqttSubAckMessage = new MqttSubAckMessage(mqttFixedHeader, variableHeader, payload);
        channel.writeAndFlush(mqttSubAckMessage);
    }
    /**
     * 创建客户端订阅主题反馈
     * @param msgId
     * @param grantedQoSList
     * @return
     */
//    private static MqttSubAckMessage createSubAckMessage(Integer msgId, List<Integer> grantedQoSList) {
//        MqttFixedHeader mqttFixedHeader =
//                new MqttFixedHeader(SUBACK, false, AT_LEAST_ONCE, false, 0);
//        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
//        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(grantedQoSList);
//        return new MqttSubAckMessage(mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);
//    }
//
//    private void registerSubQoS(String topic, List<Integer> grantedQoSList, MqttQoS reqQoS) {
//        grantedQoSList.add(getMinSupportedQos(reqQoS));
//        mqttQoSMap.put(new MqttTopicMatcher(topic), getMinSupportedQos(reqQoS));
//    }
//    private static int getMinSupportedQos(MqttQoS reqQoS) {
//        return Math.min(reqQoS.value(), MAX_SUPPORTED_QOS_LVL.value());
//    }

    /**
     * 回复pong消息
     */
    @Override
    public void pong(Channel channel) {

    }

    /**
     * 取消订阅
     */
    @Override
    public void unsubscribe(Channel channel, MqttUnsubscribeMessage mqttMessage) {

    }

    /**
     * 回复取消订阅
     */
    private void unSubBack(Channel channel, int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttUnsubAckMessage mqttUnsubAckMessage = new MqttUnsubAckMessage(mqttFixedHeader, variableHeader);
        channel.writeAndFlush(mqttUnsubAckMessage);
    }

    /**
     * disconnect 主动断线
     */
    @Override
    public void disconnect(Channel channel) {

    }

    /**
     * 关闭通道
     */
    @Override
    public void close(Channel channel) {

    }

    /**
     * 消息回复确认(qos1 级别 保证收到消息  但是可能会重复)
     */
    @Override
    public void puback(Channel channel, MqttMessage mqttMessage) {

    }

    /**
     * qos2 发布收到
     */
    @Override
    public void pubrec(Channel channel, MqttMessage mqttMessage) {

    }

    /**
     * qos2 发布释放
     */
    @Override
    public void pubrel(Channel channel, MqttMessage mqttMessage) {

    }

    /**
     * qos2 发布完成
     */
    @Override
    public void pubcomp(Channel channel, MqttMessage mqttMessage) {

    }

    @Override
    public void doTimeOut(Channel channel, IdleStateEvent evt) {

    }

    /**
     * 封装发布
     * @param str
     * @param topicName
     * @return
     */
    public static MqttPublishMessage buildPublish(String str, String topicName, Integer messageId)
    {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, AT_LEAST_ONCE, false, str.length());
        //("MQIsdp",3,false,false,false,0,false,false,60);
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(topicName, messageId);
        ByteBuf payload = Unpooled.wrappedBuffer(str.getBytes(CharsetUtil.UTF_8));
        MqttPublishMessage msg = new MqttPublishMessage(mqttFixedHeader, variableHeader, payload);
        return msg;
    }
}
