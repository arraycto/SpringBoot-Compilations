package com.wjwcloud.iot.utils.redis;


import com.wjwcloud.iot.model.IBaseService;

public interface IRedisService<V, E> extends IBaseService<V, E> {
    void setCacheThread(boolean var1);
}
