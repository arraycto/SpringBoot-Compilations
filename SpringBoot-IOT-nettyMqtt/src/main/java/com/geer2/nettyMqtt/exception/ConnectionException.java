package com.geer2.nettyMqtt.exception;

/**
 * 连接异常
 *
 **/
public class ConnectionException extends  RuntimeException {

    public ConnectionException(String message) {
        super(message);
    }
}
