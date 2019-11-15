package com.geer2.nettyprotocol.pool;

import java.util.concurrent.ScheduledFuture;

/**
 * 接口
 *
 * @author
 **/
@FunctionalInterface
public interface Scheduled {

    ScheduledFuture<?> submit(Runnable runnable);
}
