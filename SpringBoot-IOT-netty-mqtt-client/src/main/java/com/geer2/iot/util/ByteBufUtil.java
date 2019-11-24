package com.geer2.iot.util;

import io.netty.buffer.ByteBuf;

/**
 * 跨线程情况下 byteBuf 需要转换成byte[]
 *
 * @author JiaweiWu
 * @create 2019-11-29 9:07
 **/
public class ByteBufUtil {

    public  static byte[]  copyByteBuf(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
