package com.geer2.nettyprotocol.server;

import com.geer2.nettyprotocol.pool.DefaultThreadFactory;
import com.geer2.nettyprotocol.server.properties.InitBean;
import com.geer2.nettyprotocol.server.coder.ByteBufToWebSocketFrameEncoder;
import com.geer2.nettyprotocol.server.coder.WebSocketFrameToByteBufDecoder;
import com.geer2.nettyprotocol.server.ssl.SecureSocketSslContextFactory;
import com.geer2.nettyprotocol.util.SpringBeanUtils;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.lang3.ObjectUtils;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.security.KeyStore;
import java.util.concurrent.*;

/**
 * 抽象类 负责加载edec handler
 *
 * @author
 * @create
 **/
public abstract class AbstractBootstrapServer implements BootstrapServer {


    /**
     * TLS 协议
     */
    private static   String PROTOCOL = "TLS";

    private  static SSLContext SERVER_CONTEXT;

    private static final String MQTT_CSV_LIST = "mqtt, mqttv3.1, mqttv3.1.1";


    /**
     *
     * @param channelPipeline  channelPipeline
     * @param serverBean  服务配置参数
     */
    protected  void initHandler(ChannelPipeline channelPipeline, InitBean serverBean){
        if(serverBean.isSsl()){
            if(!ObjectUtils.allNotNull(serverBean.getJksCertificatePassword(),serverBean.getJksFile(),serverBean.getJksStorePassword())){
                throw  new NullPointerException("SSL file and password is null");
            }
            initSsl(serverBean);
            SSLEngine engine =
                    SERVER_CONTEXT.createSSLEngine();
            engine.setUseClientMode(false);
            channelPipeline.addLast("ssl", new SslHandler(engine));
        }

        intProtocolHandler(channelPipeline,serverBean);
        channelPipeline.addLast(new IdleStateHandler(serverBean.getHeart(),0,0));
        channelPipeline.addLast(  SpringBeanUtils.getBean(serverBean.getMqttHander()));

    }

    private  void intProtocolHandler(ChannelPipeline channelPipeline, InitBean serverBean){
                switch (serverBean.getProtocol()){
                    case MQTT:
                        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                        channelPipeline.addLast("decoder", new MqttDecoder());
                        break;
                    case MQTT_WS_MQTT:
                        channelPipeline.addLast("httpCode", new HttpServerCodec());
                        channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                        channelPipeline.addLast("webSocketHandler",
                                new WebSocketServerProtocolHandler("/", MQTT_CSV_LIST));
                        channelPipeline.addLast("wsDecoder", new WebSocketFrameToByteBufDecoder());
                        channelPipeline.addLast("wsEncoder", new ByteBufToWebSocketFrameEncoder());
                        channelPipeline.addLast("decoder", new MqttDecoder());
                        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                        break;
                    case MQTT_WS_PAHO:
                        channelPipeline.addLast("httpCode", new HttpServerCodec());
                        channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                        channelPipeline.addLast("webSocketHandler",
                                new WebSocketServerProtocolHandler("/mqtt", MQTT_CSV_LIST));
                        channelPipeline.addLast("wsDecoder", new WebSocketFrameToByteBufDecoder());
                        channelPipeline.addLast("wsEncoder", new ByteBufToWebSocketFrameEncoder());
                        channelPipeline.addLast("decoder", new MqttDecoder());
                        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                        break;
                    default:
                        break;
                }
    }

    private void initSsl(InitBean serverBean){
        ThreadFactory namedThreadFactory = new DefaultThreadFactory("ssl");
        ExecutorService singleThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L,TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),namedThreadFactory);
        singleThreadPool.submit(() -> {});
        String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        SSLContext serverContext;
        try {
            //
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(  SecureSocketSslContextFactory.class.getResourceAsStream(serverBean.getJksFile()),
                    serverBean.getJksStorePassword().toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks,serverBean.getJksCertificatePassword().toCharArray());
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), null, null);
        } catch (Exception e) {
            throw new Error(
                    "Failed to initialize the server-side SSLContext", e);
        }
        SERVER_CONTEXT = serverContext;
    }
}
