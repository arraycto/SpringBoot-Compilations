package com.geer2.nettyMqtt.util;

import io.netty.buffer.ByteBuf;

/**
 * 跨线程情况下 byteBuf 需要转换成byte[]
 *
 * @author
 * @create
 **/
public class ByteBufUtil {

    public  static byte[]  copyByteBuf(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
