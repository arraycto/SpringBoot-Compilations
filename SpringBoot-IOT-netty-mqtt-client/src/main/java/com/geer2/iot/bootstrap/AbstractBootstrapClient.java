package com.geer2.iot.bootstrap;

import com.geer2.iot.bootstrap.handler.BaseMqttHander;
import com.geer2.iot.commons.properties.ConnectOptions;
import com.geer2.iot.commons.ssl.SecureSokcetTrustManagerFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * 抽象类
 *
 * @author JiaweiWu
 * @create 2019-12-21 15:56
 **/
public abstract class AbstractBootstrapClient implements  BootstrapClient {


    private SSLContext clientContext;

    private static  String PROTOCOL = "TLS";

    /**
     *  @param channelPipeline  channelPipeline
     * @param clientBean  客户端配置参数
     */
    protected  void initHandler(ChannelPipeline channelPipeline, ConnectOptions clientBean, BaseMqttHander baseMqttHander){
        if(clientBean.isSsl()){
            initSsl();
            SSLEngine engine =
                    clientContext.createSSLEngine();
            engine.setUseClientMode(true);
            channelPipeline.addLast("ssl", new SslHandler(engine));
        }
        channelPipeline.addLast("decoder", new MqttDecoder());
        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
        channelPipeline.addLast(new IdleStateHandler(clientBean.getHeart(),0,0));
        channelPipeline.addLast(baseMqttHander);

    }

    private void initSsl(){
        SSLContext clientContext;
        try {
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(null, SecureSokcetTrustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error(
                    "Failed to initialize the client-side SSLContext", e);
        }
        clientContext = clientContext;
    }
}
