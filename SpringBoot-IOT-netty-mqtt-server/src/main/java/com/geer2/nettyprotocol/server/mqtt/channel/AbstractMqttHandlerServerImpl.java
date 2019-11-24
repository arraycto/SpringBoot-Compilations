package com.geer2.nettyprotocol.server.mqtt.channel;

import com.geer2.nettyprotocol.server.bean.MqttChannel;
import com.geer2.nettyprotocol.server.mqtt.api.BaseAuthService;
import com.geer2.nettyprotocol.server.mqtt.api.ChannelService;
import com.geer2.nettyprotocol.server.mqtt.api.AbstractMqttHandlerService;
import com.geer2.nettyprotocol.server.mqtt.constant.MqttChannelConstant;
import com.geer2.nettyprotocol.server.mqtt.utils.MqttTopicMatcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static io.netty.handler.codec.mqtt.MqttQoS.AT_LEAST_ONCE;

/**
 * @author JiaweiWu
 */
@Component
public class AbstractMqttHandlerServerImpl extends AbstractMqttHandlerService {

    @Qualifier("channelServiceImpl")
    @Autowired
    ChannelService mqttChannelService;

    @Autowired
    private BaseAuthService baseAuthService;



    public static Logger log = LogManager.getLogger(AbstractMqttHandlerServerImpl.class);

    public static final MqttQoS MAX_SUPPORTED_QOS_LVL = MqttQoS.AT_LEAST_ONCE;

    private final ConcurrentMap<MqttTopicMatcher,Integer> mqttQoSMap = new ConcurrentHashMap<>();

    @Override
    public boolean login(Channel channel, MqttConnectMessage mqttConnectMessage) {
        //   校验规则 自定义校验规则
        MqttConnectPayload payload = mqttConnectMessage.payload();
        String deviceId = payload.clientIdentifier();
        if (StringUtils.isBlank(deviceId)) {
            MqttConnectReturnCode connectReturnCode = MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED;
            connectBack(channel,connectReturnCode);
            return false;
        }

        if(mqttConnectMessage.variableHeader().hasPassword() && mqttConnectMessage.variableHeader().hasUserName()
                && !baseAuthService.authorized(payload.userName(),new String(payload.passwordInBytes(), CharsetUtil.UTF_8))){
            MqttConnectReturnCode connectReturnCode = MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD;
            connectBack(channel,connectReturnCode);
            return false;
        }
        return  Optional.ofNullable(mqttChannelService.getMqttChannel(deviceId))
                .map(mqttChannel -> {
                    switch (mqttChannel.getSessionStatus()){
                        case OPEN:
                            return false;
                        default:break;
                    }
                    mqttChannelService.loginSuccess(channel, deviceId, mqttConnectMessage);
                    return true;
                }).orElseGet(() -> {
                    mqttChannelService.loginSuccess(channel, deviceId, mqttConnectMessage);
                    return  true;
                });

    }

    private  void  connectBack(Channel channel,  MqttConnectReturnCode connectReturnCode){
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(connectReturnCode, true);
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.CONNACK,false, MqttQoS.AT_MOST_ONCE, false, 0x02);
        MqttConnAckMessage connAck = new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
        channel.writeAndFlush(connAck);
    }

    /**
     *  客户端发布消息
     * @param channel
     * @param mqttPublishMessage
     */
    @Override
    public void publish(Channel channel, MqttPublishMessage mqttPublishMessage) {
        mqttChannelService.publishSuccess(channel, mqttPublishMessage);
//        ByteBuf buf = mqttPublishMessage.payload();
//        String msg = new String(ByteBufUtil.getBytes(buf));
//        log.debug("终端消息上报 start，终端编码为："+channel.attr(DeviceManage.DEVICE).get()+" 终端上报消息体："+msg);
//        int msgId = mqttPublishMessage.variableHeader().messageId();
//        if (msgId == -1) {
//            msgId = 1;
//        }
//        //主题名
//        String topicName = mqttPublishMessage.variableHeader().topicName();
//        System.out.println("上报消息的主题："+topicName);
//        try
//        {
//            //上报消息写入
//            StbReportMsg stbmsg= GsonJsonUtil.fromJson(msg, StbReportMsg.class);
//            //机顶盒编号||消息编号||发送状态||点击状态 ||更新时间||消息应下发用户总数
//            if(!StringUtils.isEmpty(stbmsg.getMsgId()))
//            {
//                UpMessage upmessage=new UpMessage();
//                upmessage.setDeviceId(StringUtils.isEmpty(stbmsg.getDeviceNum())?channel.attr(DeviceManage.DEVICE).get():stbmsg.getDeviceNum());
//                upmessage.setMsgCode(stbmsg.getMsgId());
//                upmessage.setStatus(stbmsg.getStatus());
//                upmessage.setIsOnclick(stbmsg.getJumpFlag());
//                upmessage.setDate(UpMessage.getCurrentDate());
//                upmessage.setMsgType(stbmsg.getMsgType());
//                if(HttpServerHandler.messageMap.containsKey(stbmsg.getMsgId()))
//                {
//                    upmessage.setUserNums(HttpServerHandler.messageMap.get(stbmsg.getMsgId()).getUserNumbers());
//                }
//                log.debug("终端消息上报 end 终端上报消息成功。终端编号："+channel.attr(DeviceManage.DEVICE).get()+" 消息编码："+stbmsg.getMsgId()+"消息状态："+stbmsg.getStatus());
//                HttpServerHandler.reportMsgLog.debug(upmessage.getDeviceId()+"||"+upmessage.getMsgCode()+"||"
//                        +upmessage.getStatus()+"||"+upmessage.getIsOnclick()+"||"+upmessage.getDate()
//                        +"||"+upmessage.getUserNums()+"||"+upmessage.getMsgType());
//            }else
//            {
//                log.error("终端消息上报 end 终端上报消息编码为空！终端编号为: "+ channel.attr(DeviceManage.DEVICE).get()+" 上报消息为： "+msg);
//            }
//        }
//        catch (JsonSyntaxException e)
//        {
//            log.error("终端消息上报 end 终端上报消息格式错误！终端编号为: "+channel.attr(DeviceManage.DEVICE).get()+" 上报消息为： "+msg);
//        }
//
//        if (mqttPublishMessage.fixedHeader().qosLevel() == AT_LEAST_ONCE)
//        {
//            MqttMessageIdVariableHeader header = MqttMessageIdVariableHeader.from(msgId);
//            MqttPubAckMessage puback = new MqttPubAckMessage(Constants.PUBACK_HEADER, header);
//            channel.write(puback);
//        }
//        msg = null;
//        topicName = null;
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
        List<Integer> grantedQosLevels = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            grantedQosLevels.add(mqttSubscribeMessage.payload().topicSubscriptions().get(i).qualityOfService().value());
        }
        MqttSubAckPayload payload = new MqttSubAckPayload(grantedQosLevels);
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
        mqttChannelService.closeSuccess(mqttChannelService.getDeviceId(channel), true);
    }

    /**
     * 关闭通道
     */
    @Override
    public void close(Channel channel) {
        mqttChannelService.closeSuccess(mqttChannelService.getDeviceId(channel), false);
        channel.close();
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
        log.info("【PingPongService：doTimeOut 心跳超时】" + channel.remoteAddress() + "【channel 关闭】");
        switch (evt.state()) {
            case READER_IDLE:
                close(channel);
                break;
            case WRITER_IDLE:
                close(channel);
                break;
            case ALL_IDLE:
                close(channel);
                break;
            default:break;
        }
    }

    /**
     * 服务端监听了读空闲(间隔为5秒一次)，在触发读空闲的时候，
     * 服务端需要向客户端写一个心跳数据包，并累计空闲次数，如果超过3次，就认为客户端失连；
     *
     * 客户端收到服务端的心跳消息后，会回写一个心跳包给服务端，
     * 服务端收到消息后，会将该客户端的空闲次数清0
     * @param channel
     * @param evt
     */
    @Override
    public void doTimeOutEvt(Channel channel, IdleStateEvent evt) {
        //读取空闲
        if (evt.state().equals(IdleState.READER_IDLE)){
            MqttChannel mqttChannel = mqttChannelService.getMqttChannel(mqttChannelService.getDeviceId(channel));
            int readerNum = mqttChannel.getReaderNum();
            int num = ++readerNum;
            mqttChannel.setReaderNum(num);
            log.debug("发送心跳给客户端！");
            buildHearBeat(channel);
            System.out.println("发送心跳次数："+ mqttChannel.getReaderNum());
            if (num > MqttChannelConstant.NUM_4){
                close(channel);
            }
        }
    }
    /**
     * 封装心跳请求
     * @param channel
     */
    private void buildHearBeat(Channel channel) {
        MqttFixedHeader mqttFixedHeader=new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessage message=new MqttMessage(mqttFixedHeader);
        channel.writeAndFlush(message);
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