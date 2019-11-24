package com.geer2.iot.pool;

import java.util.concurrent.ScheduledFuture;

/**
 * 接口
 *
 * @author JiaweiWu
 **/
@FunctionalInterface
public interface Scheduled {

    /**
     *
     * @param runnable
     * @return
     */
    ScheduledFuture<?> submit(Runnable runnable);
}
