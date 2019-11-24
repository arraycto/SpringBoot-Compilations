package com.geer2.nettyprotocol.server;

import com.geer2.nettyprotocol.server.properties.InitBean;

/**
 * 启动类接口
 *
 * @author
 **/
public interface BootstrapServer {

    /**
     * 关闭服务
     */
    void shutdown();

    /**
     * 服务配置
     * @param serverBean
     */
    void setServerBean(InitBean serverBean);

    /**
     * 启动服务
     */
    void start();


}
