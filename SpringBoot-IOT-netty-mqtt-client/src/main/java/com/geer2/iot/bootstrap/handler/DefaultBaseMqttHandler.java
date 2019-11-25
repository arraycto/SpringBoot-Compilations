package com.geer2.iot.bootstrap.handler;

import com.geer2.iot.auto.MqttListener;
import com.geer2.iot.bootstrap.producer.MqttProducer;
import com.geer2.iot.bootstrap.producer.Producer;
import com.geer2.iot.bootstrap.api.AbstractClientMqttHandlerService;
import com.geer2.iot.properties.ConnectOptions;
import com.geer2.iot.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 默认 mqtthandler处理
 *
 * @author JiaweiWu
 * @create 2019-11-20 13:58
 **/

@ChannelHandler.Sharable
@Slf4j
public class DefaultBaseMqttHandler extends BaseMqttHander {


    private AbstractClientMqttHandlerService mqttHandlerApi;

    private MqttProducer mqttProducer;

    private MqttListener mqttListener;
    
    private ConnectOptions connectOptions;

    public DefaultBaseMqttHandler(ConnectOptions connectOptions, AbstractClientMqttHandlerService mqttHandlerApi, Producer producer, MqttListener mqttListener) {
        super(mqttHandlerApi);
        this.mqttProducer =(MqttProducer) producer;
        this.mqttListener = mqttListener;
        this.mqttHandlerApi=mqttHandlerApi;
        this.connectOptions=connectOptions;
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ConnectOptions.MqttOpntions mqtt = connectOptions.getMqtt();
        log.info("【DefaultBaseMqttHandler：channelActive】"+ctx.channel().localAddress().toString()+"启动成功");
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT,false, MqttQoS.AT_LEAST_ONCE,false,10);
        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_3_1_1.protocolName(),MqttVersion.MQTT_3_1_1.protocolLevel(),mqtt.isHasUserName(),mqtt.isHasPassword(),mqtt.isHasWillRetain(),mqtt.getWillQos(),mqtt.isHasWillFlag(),mqtt.isHasCleanSession(),mqtt.getKeepAliveTime());
        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(mqtt.getClientIdentifier(),mqtt.getWillTopic(),mqtt.getWillMessage().getBytes(Charset.forName("UTF-8")),mqtt.getUserName(),mqtt.getPassword().getBytes(Charset.forName("UTF-8")));
        MqttConnectMessage mqttSubscribeMessage = new MqttConnectMessage(mqttFixedHeader,mqttConnectVariableHeader,mqttConnectPayload);
        channel.writeAndFlush(mqttSubscribeMessage);
    }



    @Override
    public void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        switch (mqttFixedHeader.messageType()){
            case UNSUBACK:
                mqttHandlerApi.unsubBack(channelHandlerContext.channel(),mqttMessage);
                break;
            case CONNACK:
                mqttProducer.connectBack((MqttConnAckMessage) mqttMessage);
                break;
            case PUBLISH:
                publish(channelHandlerContext.channel(),(MqttPublishMessage)mqttMessage);
                break;
            // qos 1回复确认
            case PUBACK:
                mqttHandlerApi.puback(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBREC:
                mqttHandlerApi.pubrec(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBREL:
                mqttHandlerApi.pubrel(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBCOMP:
                mqttHandlerApi.pubcomp(channelHandlerContext.channel(),mqttMessage);
                break;
            case SUBACK:
                mqttHandlerApi.suback(channelHandlerContext.channel(),(MqttSubAckMessage)mqttMessage);
                break;
            default:
                break;
        }
    }

    private void publish(Channel channel,MqttPublishMessage mqttMessage) {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        MqttPublishVariableHeader mqttPublishVariableHeader = mqttMessage.variableHeader();
        ByteBuf payload = mqttMessage.payload();
        byte[] bytes = ByteBufUtil.copyByteBuf(payload);
        if(mqttListener!=null){
            mqttListener.callBack(mqttPublishVariableHeader.topicName(),new String(bytes));
        }
        switch (mqttFixedHeader.qosLevel()){
            case AT_MOST_ONCE:
                break;
            case AT_LEAST_ONCE:
                mqttHandlerApi.pubBackMessage(channel,mqttPublishVariableHeader.packetId());
                break;
            case EXACTLY_ONCE:
                mqttProducer.pubRecMessage(channel,mqttPublishVariableHeader.packetId());
                break;
            default:break;
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(mqttListener!=null){
            mqttListener.callThrowable(cause);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        mqttProducer.getNettyBootstrapClient().doubleConnect();
    }
}
