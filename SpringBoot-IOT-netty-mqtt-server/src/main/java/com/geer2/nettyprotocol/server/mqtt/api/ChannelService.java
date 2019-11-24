package com.geer2.nettyprotocol.server.mqtt.api;

import com.geer2.nettyprotocol.server.bean.MqttChannel;
import com.geer2.nettyprotocol.server.bean.WillMeaasge;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.List;
import java.util.Set;

/**
 * 消息处理
 *
 * @author JiaweiWu
 * @create
 **/
public interface ChannelService {

    /**
     * 获取Channel
     * @param deviceId
     * @return
     */
    MqttChannel getMqttChannel(String deviceId);

    /**
     * 连接成功处理
     * @param s
     * @param build
     * @return
     */
    boolean connectSuccess(String s, MqttChannel build);


    /**
     * 订阅成功处理
     * @param deviceId
     * @param topics
     */
    void suscribeSuccess(String deviceId, Set<String> topics);


    /**
     * 登录成功处理
     * @param channel
     * @param deviceId
     * @param mqttConnectMessage
     */
    void loginSuccess(Channel channel, String deviceId, MqttConnectMessage mqttConnectMessage);

    /**
     * 从客户端向服务端或者服务端向客户端传输一个应用消息。
     * 推送成功处理
     * @param channel
     * @param mqttPublishMessage
     */
    void publishSuccess(Channel channel, MqttPublishMessage mqttPublishMessage);

    /**
     * 关闭成功处理
     * @param deviceId
     * @param isDisconnect
     */
    void closeSuccess(String deviceId, boolean isDisconnect);

    /**
     * 发送遗嘱
     * @param willMeaasge
     */
    void sendWillMsg(WillMeaasge willMeaasge);

    /**
     * 获取deviceId
     * @param channel
     * @return
     */
    String  getDeviceId(Channel channel);

    /**
     * 取消订阅
     * @param deviceId
     * @param topics1
     */
    void unsubscribe(String deviceId, List<String> topics1);

    /**
     * ( QoS 2， 消息确认响应第二步）：
     *     PUBREC报文是对QoS等级2的PUBLISH报文的响应。 它是QoS 2等级协议交换的第二个报文。
     * @param channel
     * @param mqttMessage
     */
    void  doPubrec(Channel channel, int mqttMessage);

    /**
     * （ QoS 2， 消息确认响应第三步）：PUBREL报文是对PUBREC报文的响应。 它是QoS 2等级协议交换的第三个报文。
     * qos2 第三步
     * @param channel
     * @param mqttMessage
     */
    void  doPubrel(Channel channel, int mqttMessage);


}
