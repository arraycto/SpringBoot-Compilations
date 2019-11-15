package com.geer2.nettyprotocol.server.properties;

import com.geer2.nettyprotocol.server.mqtt.constant.enums.ProtocolEnum;
import com.geer2.nettyprotocol.server.mqtt.handler.MqttServerHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * netty服务启动参数
 *
 * @author
 * @create
 **/
@ConfigurationProperties(prefix ="iot.server")
@Data
public class InitBean {

    private ProtocolEnum protocol;

    private int port ;

    private String serverName ;

    private boolean keepalive ;

    private boolean reuseaddr ;


    private boolean tcpNodelay ;

    private int backlog ;

    private  int  sndbuf ;

    private int revbuf ;


    private int heart ;

    private boolean ssl ;

    private String jksFile;

    private String jksStorePassword;

    private String jksCertificatePassword;

    private Class<MqttServerHandler> mqttHander ;


    private int  initalDelay ;

    private  int period ;

    private int bossThread;

    private int workThread;

}
