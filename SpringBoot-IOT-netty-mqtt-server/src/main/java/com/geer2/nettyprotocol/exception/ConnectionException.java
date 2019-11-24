package com.geer2.nettyprotocol.exception;

/**
 * 连接异常
 * @author JiaweiWu
 **/
public class ConnectionException extends  RuntimeException {

    public ConnectionException(String message) {
        super(message);
    }
}
