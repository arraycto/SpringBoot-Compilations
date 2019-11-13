package com.geer2.nettyMqtt.server.queue;

import com.lmax.disruptor.RingBuffer;

public interface MessageStarter<T> {

    RingBuffer<T> getRingBuffer();

    void shutdown();

}
