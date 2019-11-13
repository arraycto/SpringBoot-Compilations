package com.geer2.nettyMqtt.server.channel;

import com.geer2.nettyMqtt.bean.MqttChannel;
import com.geer2.nettyMqtt.server.api.ChannelService;
import com.geer2.nettyMqtt.server.channel.cache.CacheMap;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象类
 *
 * @author
 * @create
 **/
@Slf4j
@Component
public  class AbstractChannelService  implements ChannelService {


//    protected AttributeKey<Boolean> _login = AttributeKey.valueOf("login");

    protected AttributeKey<String> _device = AttributeKey.valueOf("device");

    protected  static char SPLITOR = '/';

//    protected ExecutorService executorService =Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);


    protected static CacheMap<String, MqttChannel> cacheMap= new CacheMap<>();

    // deviceId - mqChannel 登录
    protected static ConcurrentHashMap<String ,MqttChannel> mqttChannels = new ConcurrentHashMap<>();

    // topic - 保留消息
//    protected  static  ConcurrentHashMap<String,ConcurrentLinkedQueue<RetainMessage>> retain = new ConcurrentHashMap<>();



    protected  static  Cache<String, Collection<MqttChannel>> mqttChannelCache = CacheBuilder.newBuilder().maximumSize(100).build();

//    public AbstractChannelService(MessageTransfer transfer ) {
//        super(transfer);
//    }


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
            return cacheMap.putData(getTopic(s),mqttChannel);
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

    @Override
    public void suscribeSuccess(String deviceId, Set<String> topics) {

    }

    @Override
    public void loginSuccess(Channel channel, String deviceId, MqttConnectMessage mqttConnectMessage) {

    }

    @Override
    public void publishSuccess(Channel channel, MqttPublishMessage mqttPublishMessage) {

    }

    @Override
    public void closeSuccess(String deviceId, boolean isDisconnect) {

    }

    /**
     * 获取channelId
     */
    @Override
    public String  getDeviceId(Channel channel){
        return  Optional.ofNullable(channel).map( channel1->channel1.attr(_device).get())
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



}
