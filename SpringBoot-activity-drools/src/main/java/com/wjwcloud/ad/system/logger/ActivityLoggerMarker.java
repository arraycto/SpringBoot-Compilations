package com.wjwcloud.ad.system.logger;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * ActivityLoggerMarker
 */
public class ActivityLoggerMarker {
    /**
     * 超时警告
     */
    public static final Marker TIME_OUT = MarkerFactory.getMarker("time_out");
    /**
     * 异常处理
     */
    public static final Marker EXCEPTION_HANDLER = MarkerFactory.getMarker("exception_handler");

    public static final Marker BUSINESS = MarkerFactory.getMarker("business");
}
