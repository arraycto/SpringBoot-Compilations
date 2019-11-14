package com.geer2.nettyMqtt.auto;

import com.geer2.nettyMqtt.properties.InitBean;
import com.geer2.nettyMqtt.server.BootstrapServer;
import com.geer2.nettyMqtt.server.NettyBootstrapServer;

/**
 * 初始化服务
 *
 * @author lxr
 * @create 2017-11-29 20:12
 **/
public class InitServer {

    private InitBean serverBean;

    public InitServer(InitBean serverBean) {
        this.serverBean = serverBean;
    }

    BootstrapServer bootstrapServer;

    public void open(){
        if(serverBean!=null){
            bootstrapServer = new NettyBootstrapServer();
            bootstrapServer.setServerBean(serverBean);
            bootstrapServer.start();
        }
    }


    public void close(){
        if(bootstrapServer!=null){
            bootstrapServer.shutdown();
        }
    }

}
