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
     * 定时任务处理
     * @param runnable
     * @return
     */
    ScheduledFuture<?> submit(Runnable runnable);
}
