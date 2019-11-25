package com.geer2.iot.bootstrap.handler;

import com.geer2.iot.bootstrap.api.MqttHandlerIntf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * mqtt协议处理器
 *
 * @author JiaweiWu
 * @create 2019-11-25 13:38
 **/
@Slf4j
public  abstract  class BaseMqttHander extends SimpleChannelInboundHandler<MqttMessage> {

    MqttHandlerIntf mqttHandlerApi;

    public BaseMqttHander(MqttHandlerIntf mqttHandlerIntf){
        this.mqttHandlerApi=mqttHandlerIntf;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        Optional.ofNullable(mqttFixedHeader)
                .ifPresent(mqttFixedHeader1 -> doMessage(channelHandlerContext,mqttMessage));
    }

    /**
     * 处理
     * @param channelHandlerContext
     * @param mqttMessage
     */
    public  abstract void doMessage(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage);




    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("【DefaultBaseMqttHandler：channelInactive】"+ctx.channel().localAddress().toString()+"关闭成功");
        mqttHandlerApi.close(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            mqttHandlerApi.doTimeOut(ctx.channel(),(IdleStateEvent)evt);
        }
        super.userEventTriggered(ctx, evt);
    }


}
