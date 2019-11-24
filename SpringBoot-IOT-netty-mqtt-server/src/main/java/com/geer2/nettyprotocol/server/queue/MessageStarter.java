package com.geer2.nettyprotocol.server.queue;

import com.lmax.disruptor.RingBuffer;
/**
 * @author JiaweiWu
 */
public interface MessageStarter<T> {

    /**
     * 将数据装入RingBuffer
     * @return
     */
    RingBuffer<T> getRingBuffer();

    /**
     * 关闭 disruptor  阻塞直至所有事件都得到处理
     */
    void shutdown();

}
