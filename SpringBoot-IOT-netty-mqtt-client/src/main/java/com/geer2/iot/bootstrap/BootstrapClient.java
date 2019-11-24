package com.geer2.iot.bootstrap;


import io.netty.channel.Channel;

/**
 * 启动类接口
 *
 * @author JiaweiWu
 * @create 2019-11-18 14:05
 **/
public interface BootstrapClient {


    void shutdown();

    void initEventPool();

    Channel start();


}
