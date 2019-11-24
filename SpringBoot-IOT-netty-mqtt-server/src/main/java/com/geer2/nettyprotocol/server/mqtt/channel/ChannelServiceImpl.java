package com.geer2.nettyprotocol.server.mqtt.channel;

import com.geer2.nettyprotocol.exception.ConnectionException;
import com.geer2.nettyprotocol.server.bean.*;
import com.geer2.nettyprotocol.server.mqtt.api.BaseApi;
import com.geer2.nettyprotocol.server.mqtt.api.ChannelService;
import com.geer2.nettyprotocol.server.mqtt.channel.cache.CacheMap;
import com.geer2.nettyprotocol.server.mqtt.constant.enums.ConfirmStatus;
import com.geer2.nettyprotocol.server.mqtt.constant.enums.SessionStatus;
import com.geer2.nettyprotocol.server.mqtt.constant.enums.SubStatus;
import com.geer2.nettyprotocol.server.queue.MessageTransfer;
import com.geer2.nettyprotocol.util.ByteBufUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.handler.codec.mqtt.MqttQoS.EXACTLY_ONCE;

/**
 * @author JiaweiWu
 * @create
 **/
@Slf4j
@Component
public  class ChannelServiceImpl extends PublishApiSevice implements ChannelService, BaseApi {

    @Autowired
    private ClientSessionService clientSessionService;

    @Autowired
    private WillService willService;


    protected  static char SPLITOR = '/';

    protected AttributeKey<Boolean> LOGIN = AttributeKey.valueOf("login");

    protected   AttributeKey<String> DEVICE_ID = AttributeKey.valueOf("deviceId");

    protected ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    /**
     * deviceId - mqChannel 设备数据缓存
     */
    public static ConcurrentHashMap<String , MqttChannel> mqttChannels = new ConcurrentHashMap<>();

    /**
     * topic--mqttChannel
     */
    protected static CacheMap<String, MqttChannel> cacheMap= new CacheMap<>();

    protected  static Cache<String, Collection<MqttChannel>> mqttChannelCache = CacheBuilder.newBuilder().maximumSize(100).build();

    /**
     * topic - 保留消息
     */
    protected  static ConcurrentHashMap<String, ConcurrentLinkedQueue<RetainMessage>> retain = new ConcurrentHashMap<>();

    public ChannelServiceImpl(MessageTransfer transfer) {
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
        /**
         * TopicFilter
         * @param topic
         * @return
         */
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
    public boolean connectSuccess(String deviceId, MqttChannel build) {
        return  Optional.ofNullable(mqttChannels.get(deviceId))
                .map(mqttChannel -> {
                    switch (mqttChannel.getSessionStatus()){
                        case OPEN:
                            return false;
                        case CLOSE:
                            switch (mqttChannel.getSubStatus()){
                                case YES:
                                    // 清除订阅  topic
                                    deleteSubTopic(mqttChannel).stream()
                                            .forEach(s -> cacheMap.putData(getTopic(s),build));
                                    break;
                                default:break;
                            }
                        default:break;
                    }
                    mqttChannels.put(deviceId,build);
                    return true;
                }).orElseGet(() -> {
                    mqttChannels.put(deviceId,build);
                    return  true;
                });
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
        channel.attr(LOGIN).set(true);
        channel.attr(DEVICE_ID).set(deviceId);
        replyLogin(channel, mqttConnectMessage);
    }

    /**
     * 登录成功后 回复
     */
    private void replyLogin(Channel channel, MqttConnectMessage mqttConnectMessage) {
        MqttFixedHeader mqttFixedHeader1 = mqttConnectMessage.fixedHeader();
        MqttConnectVariableHeader mqttConnectVariableHeader = mqttConnectMessage.variableHeader();
        final MqttConnectPayload payload = mqttConnectMessage.payload();
        String deviceId = getDeviceId(channel);
        MqttChannel build = MqttChannel.builder().channel(channel).cleanSession(mqttConnectVariableHeader.isCleanSession())
                .deviceId(payload.clientIdentifier())
                .sessionStatus(SessionStatus.OPEN)
                .isWill(mqttConnectVariableHeader.isWillFlag())
                .subStatus(SubStatus.NO)
                .topic(new CopyOnWriteArraySet<>())
                .message(new ConcurrentHashMap<>(16))
                .receive(new CopyOnWriteArraySet<>())
                .index(new AtomicInteger(1))
                .build();
        // 初始化存储mqttchannel
        if (connectSuccess(deviceId, build)) {
            // 遗嘱消息标志
            if (mqttConnectVariableHeader.isWillFlag()) {
                String willMssage = new String(payload.willMessageInBytes());
                boolean b = doIf(mqttConnectVariableHeader, mqttConnectVariableHeader1 -> (willMssage != null)
                        , mqttConnectVariableHeader1 -> (payload.willTopic() != null));
                if (!b) {
                    throw new ConnectionException("will message and will topic is not null");
                }
                // 处理遗嘱消息
                final WillMeaasge buildWill = WillMeaasge.builder().
                        qos(mqttConnectVariableHeader.willQos())
                        .willMessage(deviceId)
                        .willTopic(payload.willTopic())
                        .isRetain(mqttConnectVariableHeader.isWillRetain())
                        .build();
                willService.save(payload.clientIdentifier(), buildWill);
            } else {
                willService.del(payload.clientIdentifier());
                boolean b = doIf(mqttConnectVariableHeader, mqttConnectVariableHeader1 -> (!mqttConnectVariableHeader1.isWillRetain()),
                        mqttConnectVariableHeader1 -> (mqttConnectVariableHeader1.willQos() == 0));
                if (!b) {
                    throw new ConnectionException("will retain should be  null and will QOS equal 0");
                }
            }
            doIfElse(mqttConnectVariableHeader, mqttConnectVariableHeader1 -> (mqttConnectVariableHeader1.isCleanSession()), mqttConnectVariableHeader1 -> {
                MqttConnectReturnCode connectReturnCode = MqttConnectReturnCode.CONNECTION_ACCEPTED;
                MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(connectReturnCode, false);
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                        MqttMessageType.CONNACK, mqttFixedHeader1.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeader1.isRetain(), 0x02);
                MqttConnAckMessage connAck = new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
                // 清理会话
                channel.writeAndFlush(connAck);
            }, mqttConnectVariableHeader1 -> {
                MqttConnectReturnCode connectReturnCode = MqttConnectReturnCode.CONNECTION_ACCEPTED;
                MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(connectReturnCode, true);
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                        MqttMessageType.CONNACK, mqttFixedHeader1.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeader1.isRetain(), 0x02);
                MqttConnAckMessage connAck = new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
                // 非清理会话
                channel.writeAndFlush(connAck);

            });         //发送 session  数据
            ConcurrentLinkedQueue<SessionMessage> sessionMessages = clientSessionService.getByteBuf(payload.clientIdentifier());
            doIfElse(sessionMessages, messages -> messages != null && !messages.isEmpty(), byteBufs -> {
                SessionMessage sessionMessage;
                while ((sessionMessage = byteBufs.poll()) != null) {
                    switch (sessionMessage.getMqttQos()) {
                        case EXACTLY_ONCE:
                            sendQosConfirmMsg(EXACTLY_ONCE,getMqttChannel(deviceId), sessionMessage.getTopic(), sessionMessage.getByteBuf());
                            break;
                        case AT_MOST_ONCE:
                            sendQos0Msg(channel, sessionMessage.getTopic(), sessionMessage.getByteBuf());
                            break;
                        case AT_LEAST_ONCE:
                            sendQosConfirmMsg(MqttQoS.AT_LEAST_ONCE,getMqttChannel(deviceId), sessionMessage.getTopic(), sessionMessage.getByteBuf());
                            break;
                        default:break;
                    }
                }

            });
        }
    }

    @Override
    public void publishSuccess(Channel channel, MqttPublishMessage mqttPublishMessage) {
        MqttFixedHeader mqttFixedHeader = mqttPublishMessage.fixedHeader();
        MqttPublishVariableHeader mqttPublishVariableHeader = mqttPublishMessage.variableHeader();
        MqttChannel mqttChannel = getMqttChannel(getDeviceId(channel));
        ByteBuf payload = mqttPublishMessage.payload();
        byte[] bytes = ByteBufUtil.copyByteBuf(payload);
        //messageId
        int messageId = mqttPublishVariableHeader.packetId();
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
                    default:break;
                }
                //是保留消息  qos >0
                if ((isRetain=mqttFixedHeader.isRetain()) && mqttFixedHeader.qosLevel() != MqttQoS.AT_MOST_ONCE) {
                    saveRetain(mqttPublishVariableHeader.topicName(),
                            RetainMessage.builder()
                                    .byteBuf(bytes)
                                    .mqttQos(mqttFixedHeader.qosLevel())
                                    .build(), false);
                    // 是保留消息 qos=0  清除之前保留消息 保留现在
                } else if (mqttFixedHeader.isRetain() && mqttFixedHeader.qosLevel() == MqttQoS.AT_MOST_ONCE) {
                    saveRetain(mqttPublishVariableHeader.topicName(),
                            RetainMessage.builder()
                                    .byteBuf(bytes)
                                    .mqttQos(mqttFixedHeader.qosLevel())
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
                    // 在线
                    case OPEN:
                        // 防止channel失效  但是离线状态没更改
                        if(subChannel.isActive()){
                            switch (qos){
                                case AT_LEAST_ONCE:
                                    sendQosConfirmMsg(MqttQoS.AT_LEAST_ONCE,subChannel,topic,bytes);
                                    break;
                                case AT_MOST_ONCE:
                                    sendQos0Msg(subChannel.getChannel(),topic,bytes);
                                    break;
                                case EXACTLY_ONCE:
                                    sendQosConfirmMsg(EXACTLY_ONCE,subChannel,topic,bytes);
                                    break;
                                default:break;
                            }
                        }
                        else{
                            if(!subChannel.isCleanSession() & !isRetain){
                                clientSessionService.saveSessionMsg(subChannel.getDeviceId(),
                                        SessionMessage.builder().byteBuf(bytes).mqttQos(qos).topic(topic).build() );
                                break;
                            }
                        }
                        break;
                    case CLOSE:
                        // 连接 设置了 clean session =false
                        clientSessionService.saveSessionMsg(subChannel.getDeviceId(),
                                SessionMessage.builder().byteBuf(bytes).mqttQos(qos).topic(topic).build() );
                        break;
                    default:break;
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
                                                    .mqttQos(sendMqttMessage.getQos())
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
                        default:break;
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
        return  Optional.ofNullable(channel).map( channel1->channel1.attr(DEVICE_ID).get())
                .orElse(null);
    }

    @Override
    public void unsubscribe(String deviceId, List<String> topics1) {
        Optional.ofNullable(mqttChannels.get(deviceId)).ifPresent(mqttChannel -> {
            topics1.forEach(topic -> {
                deleteChannel(topic,mqttChannel);
            });
        });
    }

    /**
     *  QoS 2等级协议交换的第二个报文
     */
    @Override
    public void doPubrec(Channel channel, int mqttMessage) {
        sendPubRel(channel,false,mqttMessage);
    }

    /**
     *  QoS 2等级协议交换的第三个报文
     */
    @Override
    public void doPubrel(Channel channel, int messageId) {
        MqttChannel mqttChannel = getMqttChannel(getDeviceId(channel));
        doIfElse(mqttChannel,mqttChannel1 ->mqttChannel1.isLogin(),mqttChannel1 -> {
            mqttChannel1.removeRecevice(messageId);
            sendToPubComp(channel,messageId);
        });
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
    @Override
    public void sendWillMsg(WillMeaasge willMeaasge){
        Collection<MqttChannel> mqttChannels = getChannels(willMeaasge.getWillTopic(), topic -> cacheMap.getData(getTopic(topic)));
        if(!CollectionUtils.isEmpty(mqttChannels)){
            mqttChannels.forEach(mqttChannel -> {
                switch (mqttChannel.getSessionStatus()){
                    case CLOSE:
                        clientSessionService.saveSessionMsg(mqttChannel.getDeviceId(),
                                SessionMessage.builder()
                                        .topic(willMeaasge.getWillTopic())
                                        .mqttQos(MqttQoS.valueOf(willMeaasge.getQos()))
                                        .byteBuf(willMeaasge.getWillMessage().getBytes()).build());
                        break;
                    case OPEN:
                        writeWillMsg(mqttChannel,willMeaasge);
                        break;
                    default:break;
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
        retain.forEach((topic1, retainMessages) -> {
            if(StringUtils.startsWith(topic1,topic)){
                Optional.ofNullable(retainMessages).ifPresent(pubMessages1 -> {
                    retainMessages.parallelStream().forEach(retainMessage -> {
                        log.info("【发送保留消息】"+mqttChannel.getChannel().remoteAddress()+":"+retainMessage.getString()+"【成功】");
                        switch (retainMessage.getMqttQos()){
                            case AT_MOST_ONCE:
                                sendQos0Msg(mqttChannel.getChannel(),topic1,retainMessage.getByteBuf());
                                break;
                            case AT_LEAST_ONCE:
                                sendQosConfirmMsg(MqttQoS.AT_LEAST_ONCE,mqttChannel,topic1,retainMessage.getByteBuf());
                                break;
                            case EXACTLY_ONCE:
                                sendQosConfirmMsg(EXACTLY_ONCE,mqttChannel,topic1,retainMessage.getByteBuf());
                                break;
                            default:break;
                        }
                    });
                });
            }
        });

    }


}
