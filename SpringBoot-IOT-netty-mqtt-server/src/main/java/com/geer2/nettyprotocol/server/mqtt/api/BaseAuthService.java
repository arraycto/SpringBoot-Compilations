package com.geer2.nettyprotocol.server.mqtt.api;

/**
 * 权限校验
 *
 * @author JiaweiWu
 * @create
 **/
public interface BaseAuthService {

    boolean  authorized(String username, String password);

}
