package com.geer2.nettyMqtt.server.channel;

import com.geer2.nettyMqtt.bean.*;
import com.geer2.nettyMqtt.server.api.BaseApi;
import com.geer2.nettyMqtt.server.api.ChannelService;
import com.geer2.nettyMqtt.server.channel.cache.CacheMap;
import com.geer2.nettyMqtt.server.constant.enums.ConfirmStatus;
import com.geer2.nettyMqtt.server.constant.enums.SessionStatus;
import com.geer2.nettyMqtt.server.constant.enums.SubStatus;
import com.geer2.nettyMqtt.server.queue.MessageTransfer;
import com.geer2.nettyMqtt.util.ByteBufUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 抽象类
 *
 * @author
 * @create
 **/
@Slf4j
@Component
public  class DefaultChannelService extends PublishApiSevice implements ChannelService, BaseApi {

    @Autowired
    private ClientSessionService clientSessionService;

    @Autowired
    private WillService willService;


    protected  static char SPLITOR = '/';

    protected ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    // deviceId - mqChannel 设备数据缓存
    public static ConcurrentHashMap<String ,MqttChannel> mqttChannels = new ConcurrentHashMap<>();

    //topic--mqttChannel
    protected static CacheMap<String, MqttChannel> cacheMap= new CacheMap<>();

    protected  static  Cache<String, Collection<MqttChannel>> mqttChannelCache = CacheBuilder.newBuilder().maximumSize(100).build();

    // topic - 保留消息
    protected  static ConcurrentHashMap<String, ConcurrentLinkedQueue<RetainMessage>> retain = new ConcurrentHashMap<>();

    public DefaultChannelService(MessageTransfer transfer) {
        super(transfer);
    }


    protected  Collection<MqttChannel> getChannels(String topic,TopicFilter topicFilter){
            try {
                return  mqttChannelCache.get(topic, () -> topicFilter.filter(topic));
            } catch (Exception e) {
                log.info(String.format("guava cache key topic【%s】 channel   value== null ",topic));
            }
            return null;
    }


    @FunctionalInterface
    interface TopicFilter{
        Collection<MqttChannel> filter(String topic);
    }

    protected boolean deleteChannel(String topic,MqttChannel mqttChannel){
      return  Optional.ofNullable(topic).map(s -> {
            mqttChannelCache.invalidate(s);
            return  cacheMap.delete(getTopic(s),mqttChannel);
        }).orElse(false);
    }

    protected boolean addChannel(String topic,MqttChannel mqttChannel)
    {
        return  Optional.ofNullable(topic).map(s -> {
            mqttChannelCache.invalidate(s);
            return cacheMap.putData(getTopic(s), mqttChannel);
        }).orElse(false);
    }

    /**
     * 获取channel
     */
    @Override
    public MqttChannel getMqttChannel(String deviceId){
        return Optional.ofNullable(deviceId).map(s -> mqttChannels.get(s))
                .orElse(null);

    }

    @Override
    public boolean connectSuccess(String s, MqttChannel build) {
        return false;
    }

    /**
     * 订阅成功后 (发送保留消息)
     */
    @Override
    public void suscribeSuccess(String deviceId, Set<String> topics) {
        doIfElse(topics,topics1->!CollectionUtils.isEmpty(topics1), strings -> {
            MqttChannel mqttChannel = mqttChannels.get(deviceId);
            // 设置订阅主题标识
            mqttChannel.setSubStatus(SubStatus.YES);
            mqttChannel.addTopic(strings);
            executorService.execute(() -> {
                Optional.ofNullable(mqttChannel).ifPresent(mqttChannel1 -> {
                        strings.parallelStream().forEach(topic -> {
                            addChannel(topic,mqttChannel);
                            // 发送保留消息
                            sendRetain(topic,mqttChannel);
                        });
                });
            });
        });
    }

    @Override
    public void loginSuccess(Channel channel, String deviceId, MqttConnectMessage mqttConnectMessage) {

    }

    @Override
    public void publishSuccess(Channel channel, MqttPublishMessage mqttPublishMessage) {
        MqttFixedHeader mqttFixedHeader = mqttPublishMessage.fixedHeader();
        MqttPublishVariableHeader mqttPublishVariableHeader = mqttPublishMessage.variableHeader();
        MqttChannel mqttChannel = getMqttChannel(getDeviceId(channel));
        ByteBuf payload = mqttPublishMessage.payload();
        byte[] bytes = ByteBufUtil.copyByteBuf(payload);
        int messageId = mqttPublishVariableHeader.messageId();
        executorService.execute(() -> {
            if (mqttChannel != null) {
                boolean isRetain;
                switch (mqttFixedHeader.qosLevel()) {
                    // 至多一次
                    case AT_MOST_ONCE:
                        break;
                    case AT_LEAST_ONCE:
                        sendPubBack(channel, messageId);
                        break;
                    case EXACTLY_ONCE:
                        sendPubRec(mqttChannel, messageId);
                        break;
                }
                //是保留消息  qos >0
                if ((isRetain=mqttFixedHeader.isRetain()) && mqttFixedHeader.qosLevel() != MqttQoS.AT_MOST_ONCE) {
                    saveRetain(mqttPublishVariableHeader.topicName(),
                            RetainMessage.builder()
                                    .byteBuf(bytes)
                                    .qoS(mqttFixedHeader.qosLevel())
                                    .build(), false);
                    // 是保留消息 qos=0  清除之前保留消息 保留现在
                } else if (mqttFixedHeader.isRetain() && mqttFixedHeader.qosLevel() == MqttQoS.AT_MOST_ONCE) {
                    saveRetain(mqttPublishVariableHeader.topicName(),
                            RetainMessage.builder()
                                    .byteBuf(bytes)
                                    .qoS(mqttFixedHeader.qosLevel())
                                    .build(), true);
                }
                if (!mqttChannel.checkRecevice(messageId)) {
                    push(mqttPublishVariableHeader.topicName(), mqttFixedHeader.qosLevel(), bytes,isRetain);
                    mqttChannel.addRecevice(messageId);
                }
            }
        });
    }

    /**
     * 推送消息给订阅者
     */
    private void push(String topic, MqttQoS qos, byte[] bytes, boolean isRetain){
        Collection<MqttChannel> subChannels = getChannels(topic, topic1 -> cacheMap.getData(getTopic(topic1)));
        if(!CollectionUtils.isEmpty(subChannels)){
            subChannels.parallelStream().forEach(subChannel -> {
                switch (subChannel.getSessionStatus()){
                    case OPEN: // 在线
                        if(subChannel.isActive()){ // 防止channel失效  但是离线状态没更改
                            switch (qos){
                                case AT_LEAST_ONCE:
                                    sendQosConfirmMsg(MqttQoS.AT_LEAST_ONCE,subChannel,topic,bytes);
                                    break;
                                case AT_MOST_ONCE:
                                    sendQos0Msg(subChannel.getChannel(),topic,bytes);
                                    break;
                                case EXACTLY_ONCE:
                                    sendQosConfirmMsg(MqttQoS.EXACTLY_ONCE,subChannel,topic,bytes);
                                    break;
                            }
                        }
                        else{
                            if(!subChannel.isCleanSession() & !isRetain){
                                clientSessionService.saveSessionMsg(subChannel.getDeviceId(),
                                        SessionMessage.builder().byteBuf(bytes).qoS(qos).topic(topic).build() );
                                break;
                            }
                        }
                        break;
                    case CLOSE: // 连接 设置了 clean session =false
                        clientSessionService.saveSessionMsg(subChannel.getDeviceId(),
                                SessionMessage.builder().byteBuf(bytes).qoS(qos).topic(topic).build() );
                        break;
                }
            });
        }
    }

    @Override
    public void closeSuccess(String deviceId, boolean isDisconnect) {
        if(StringUtils.isNotBlank(deviceId)){
            MqttChannel mqttChannel = mqttChannels.get(deviceId);
            Optional.ofNullable(mqttChannel).ifPresent(mqttChannel1 -> {
                // 设置关闭
                mqttChannel1.setSessionStatus(SessionStatus.CLOSE);
                // 关闭channel
                mqttChannel1.close();
                mqttChannel1.setChannel(null);
                // 保持会话
                if(!mqttChannel1.isCleanSession()){
                    // 处理 qos1 未确认数据
                    ConcurrentHashMap<Integer, SendMqttMessage> message = mqttChannel1.getMessage();
                    Optional.ofNullable(message).ifPresent(integerConfirmMessageConcurrentHashMap -> {
                        integerConfirmMessageConcurrentHashMap.forEach((integer, confirmMessage) ->
                                doIfElse(confirmMessage, sendMqttMessage ->sendMqttMessage.getConfirmStatus()== ConfirmStatus.PUB, sendMqttMessage ->{
                                    clientSessionService.saveSessionMsg(mqttChannel.getDeviceId(), SessionMessage.builder()
                                            .byteBuf(sendMqttMessage.getByteBuf())
                                            .qoS(sendMqttMessage.getQos())
                                            .topic(sendMqttMessage.getTopic())
                                            // 把待确认数据转入session中
                                            .build());
                                }
                        ));

                    });
                }
                // 删除sub topic-消息
                else{
                    // 移除channelId  不保持会话 直接删除  保持会话 旧的在重新connect时替换
                    mqttChannels.remove(deviceId);
                    switch (mqttChannel1.getSubStatus()){
                        case YES:
                            deleteSubTopic(mqttChannel1);
                            break;
                    }
                }
                // 发送遗言
                if(mqttChannel1.isWill()){
                    // 不是disconnection操作
                    if(!isDisconnect){
                        willService.doSend(deviceId);
                    }
                }
            });
        }
    }

    /**
     * 获取channelId
     */
    @Override
    public String  getDeviceId(Channel channel){
        return  Optional.ofNullable(channel).map( channel1->channel1.attr(DeviceManage.DEVICE).get())
                .orElse(null);
    }

    @Override
    public void unsubscribe(String deviceId, List<String> topics1) {

    }

    @Override
    public void doPubrel(Channel channel, int mqttMessage) {

    }

    @Override
    public void doPubrec(Channel channel, int mqttMessage) {

    }


    protected String[] getTopic(String topic)  {
        return Optional.ofNullable(topic).map(s ->
             StringUtils.split(topic,SPLITOR)
        ).orElse(null);
    }


    /**
     * 清除channel 订阅主题
     * @param mqttChannel
     */
    public Set<String>  deleteSubTopic(MqttChannel mqttChannel){
        Set<String> topics = mqttChannel.getTopic();
        topics.parallelStream().forEach(topic -> deleteChannel(topic,mqttChannel));
        return topics;
    }

    /**
     * 发送 遗嘱消息(有的channel 已经关闭 但是保持了 session  此时加入session 数据中 )
     * @param willMeaasge 遗嘱消息
     */
    public void sendWillMsg(WillMeaasge willMeaasge){
        Collection<MqttChannel> mqttChannels = getChannels(willMeaasge.getWillTopic(), topic -> cacheMap.getData(getTopic(topic)));
        if(!CollectionUtils.isEmpty(mqttChannels)){
            mqttChannels.forEach(mqttChannel -> {
                switch (mqttChannel.getSessionStatus()){
                    case CLOSE:
                        clientSessionService.saveSessionMsg(mqttChannel.getDeviceId(),
                                SessionMessage.builder()
                                        .topic(willMeaasge.getWillTopic())
                                        .qoS(MqttQoS.valueOf(willMeaasge.getQos()))
                                        .byteBuf(willMeaasge.getWillMessage().getBytes()).build());
                        break;
                    case OPEN:
                        writeWillMsg(mqttChannel,willMeaasge);
                        break;
                }
            });
        }
    }

    /**
     * 保存保留消息
     * @param topic 主题
     * @param retainMessage 信息
     */
    private void saveRetain(String topic, RetainMessage retainMessage, boolean isClean){
        ConcurrentLinkedQueue<RetainMessage> retainMessages = retain.getOrDefault(topic, new ConcurrentLinkedQueue<>());
        if(!retainMessages.isEmpty() && isClean){
            retainMessages.clear();
        }
        boolean flag;
        do{
            flag = retainMessages.add(retainMessage);
        }
        while (!flag);
        retain.put(topic, retainMessages);
    }

    /**
     * 发送保留消息
     */
    public  void sendRetain(String topic,MqttChannel mqttChannel){
        retain.forEach((_topic, retainMessages) -> {
            if(StringUtils.startsWith(_topic,topic)){
                Optional.ofNullable(retainMessages).ifPresent(pubMessages1 -> {
                    retainMessages.parallelStream().forEach(retainMessage -> {
                        log.info("【发送保留消息】"+mqttChannel.getChannel().remoteAddress()+":"+retainMessage.getString()+"【成功】");
                        switch (retainMessage.getQoS()){
                            case AT_MOST_ONCE:
                                sendQos0Msg(mqttChannel.getChannel(),_topic,retainMessage.getByteBuf());
                                break;
                            case AT_LEAST_ONCE:
                                sendQosConfirmMsg(MqttQoS.AT_LEAST_ONCE,mqttChannel,_topic,retainMessage.getByteBuf());
                                break;
                            case EXACTLY_ONCE:
                                sendQosConfirmMsg(MqttQoS.EXACTLY_ONCE,mqttChannel,_topic,retainMessage.getByteBuf());
                                break;
                        }
                    });
                });
            }
        });

    }


}
