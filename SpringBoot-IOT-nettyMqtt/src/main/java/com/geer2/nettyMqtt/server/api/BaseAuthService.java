package com.geer2.nettyMqtt.server.api;

/**
 * 权限校验
 *
 * @author JiaweiWu
 * @create
 **/
public interface BaseAuthService {

    boolean  authorized(String username, String password);

}
