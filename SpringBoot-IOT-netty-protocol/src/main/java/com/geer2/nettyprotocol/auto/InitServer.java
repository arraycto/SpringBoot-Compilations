package com.geer2.nettyprotocol.auto;

import com.geer2.nettyprotocol.server.properties.InitBean;
import com.geer2.nettyprotocol.server.BootstrapServer;
import com.geer2.nettyprotocol.server.NettyBootstrapServer;

/**
 * 初始化服务
 *
 * @author JiaweiWu
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
