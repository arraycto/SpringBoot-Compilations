package com.geer2.iot.commons.pool;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 *
 * @author JiaWeiWu
 **/
public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    /**
     * 线程分组管理
     */
    private final ThreadGroup threadGroup;
    private final AtomicInteger currentThreadNumber = new AtomicInteger(1);
    private final String namePrefix;
    /**
     * 线程优先级
     */
    private int priority = Thread.NORM_PRIORITY;
    /**
     * 是否为守护线程
     */
    private boolean isDaemon = false;


    public DefaultThreadFactory(String prefix) {
        this(prefix, false);
    }

    public DefaultThreadFactory(String prefix, boolean isDaemon) {
        this(prefix, isDaemon, Thread.NORM_PRIORITY);
    }

    public DefaultThreadFactory(String prefix, boolean isDaemon, int priority) {
        SecurityManager s = System.getSecurityManager();
        this.threadGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = prefix + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        this.isDaemon = isDaemon;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, namePrefix + currentThreadNumber.getAndIncrement(), 0);
        thread.setDaemon(isDaemon);
        thread.setPriority(priority);
        return thread;
    }
}
