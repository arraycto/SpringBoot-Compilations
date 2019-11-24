package com.geer2.nettyprotocol.server.mqtt.api;

/**
 * 权限校验
 *
 * @author JiaweiWu
 * @create
 **/
public interface BaseAuthService {

    /**
     * 登录权限验证
     * @param username
     * @param password
     * @return
     */
    boolean  authorized(String username, String password);

}
