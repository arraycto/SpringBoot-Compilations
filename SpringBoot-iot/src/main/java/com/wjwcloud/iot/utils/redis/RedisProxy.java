package com.wjwcloud.iot.utils.redis;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisProxy {
    private static final Logger logger = LoggerFactory.getLogger(RedisProxy.class);
    private boolean cacheOpen;
    private RedisTemplate<String, Object> redisTemplate;

    public RedisProxy() {
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final String key, final Object value, final long timeout) {
        if (this.cacheOpen) {
            try {
                this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        connection.del(new byte[][]{RedisProxy.this.serializeKey(key)});
                        connection.set(RedisProxy.this.serializeKey(key), RedisProxy.this.serializeValue(value));
                        if (timeout > 0L) {
                            connection.expire(RedisProxy.this.serializeKey(key), timeout);
                        }

                        return null;
                    }
                });
            } catch (Exception var6) {
                var6.printStackTrace();
                logger.error("保存缓存", var6);
            }
        }

    }

    public void save(final String key, final Object value) {
        if (this.cacheOpen) {
            try {
                this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        connection.del(new byte[][]{RedisProxy.this.serializeKey(key)});
                        connection.set(RedisProxy.this.serializeKey(key), RedisProxy.this.serializeValue(value));
                        return null;
                    }
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public Object read(final String key) {
        if (this.cacheOpen) {
            logger.debug("读取缓存key=" + key);

            try {
                return this.redisTemplate.hasKey(key) ? this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        byte[] keyBytes = RedisProxy.this.serializeKey(key);
                        byte[] valueBytes = connection.get(keyBytes);
                        return RedisProxy.this.deserializeValue(valueBytes);
                    }
                }) : null;
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Long delete(final String key) {
        if (this.cacheOpen) {
            try {
                return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
                    public Long doInRedis(RedisConnection connection) {
                        return connection.del(new byte[][]{RedisProxy.this.serializeKey(key)});
                    }
                });
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Boolean hSet(final String key, final byte[] field, final byte[] value) {
        if (this.cacheOpen) {
            try {
                return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
                    public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                        return connection.hSet(RedisProxy.this.serializeKey(key), field, value);
                    }
                });
            } catch (Exception var5) {
                var5.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public Object hGet(final String key, final byte[] field) {
        if (this.cacheOpen) {
            try {
                return this.redisTemplate.execute(new RedisCallback<Object>() {
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        byte[] value = connection.hGet(RedisProxy.this.serializeKey(key), field);
                        return RedisProxy.this.deserializeValue(value);
                    }
                });
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public void hmSet(final String key, final Map<Object, Object> mapObject) {
        if (this.cacheOpen) {
            try {
                this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        Map<byte[], byte[]> mapByte = new HashMap(mapObject.size());
                        Iterator var3 = mapObject.entrySet().iterator();

                        while(var3.hasNext()) {
                            Entry<Object, Object> entry = (Entry)var3.next();
                            byte[] mapKey = RedisProxy.this.serializeValue(entry.getKey());
                            byte[] mapValue = RedisProxy.this.serializeValue(entry.getValue());
                            mapByte.put(mapKey, mapValue);
                        }

                        connection.hMSet(RedisProxy.this.serializeKey(key), mapByte);
                        mapByte = null;
                        return null;
                    }
                });
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public List<Object> hmGet(final String key, final Object... field) {
        if (this.cacheOpen) {
            try {
                return (List)this.redisTemplate.execute(new RedisCallback<List<Object>>() {
                    @Override
                    public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                        List<byte[]> redisRet = null;
                        List<Object> ret = new ArrayList();
                        if (field.length > 1) {
                            byte[][] fields = new byte[field.length][];
                            int i = 0;
                            Object[] var6 = field;
                            int var7 = var6.length;

                            for(int var8 = 0; var8 < var7; ++var8) {
                                Object object = var6[var8];
                                fields[i] = RedisProxy.this.serializeValue(object);
                                ++i;
                            }

                            redisRet = connection.hMGet(RedisProxy.this.serializeKey(key), fields);
                        } else {
                            redisRet = connection.hMGet(RedisProxy.this.serializeKey(key), new byte[][]{RedisProxy.this.serializeValue(field[0])});
                        }

                        if (null != redisRet) {
                            Iterator var10 = redisRet.iterator();

                            while(var10.hasNext()) {
                                byte[] value = (byte[])var10.next();
                                if (value != null) {
                                    ret.add(RedisProxy.this.deserializeValue(value));
                                }
                            }
                        }

                        return ret.size() > 0 ? ret : null;
                    }
                });
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Map<Object, Object> hGetAll(final String key) {
        if (this.cacheOpen) {
            try {
                return (Map)this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        Map<byte[], byte[]> mapByte = connection.hGetAll(RedisProxy.this.serializeKey(key));
                        Map<Object, Object> mapObject = new HashMap(mapByte.size());
                        Iterator var4 = mapByte.entrySet().iterator();

                        while(var4.hasNext()) {
                            Entry<byte[], byte[]> entry = (Entry)var4.next();
                            Object mapKey = RedisProxy.this.deserializeValue((byte[])entry.getKey());
                            Object mapValue = RedisProxy.this.deserializeValue((byte[])entry.getValue());
                            mapObject.put(mapKey, mapValue);
                        }

                        mapByte = null;
                        return mapObject;
                    }
                });
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Long lPush(final String key, final Object... values) {
        if (this.cacheOpen) {
            try {
                return (Long)this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        Long count = new Long(0L);
                        Object[] var3 = values;
                        int var4 = var3.length;

                        for(int var5 = 0; var5 < var4; ++var5) {
                            Object value = var3[var5];
                            byte[] keyByte = RedisProxy.this.serializeKey(key);
                            byte[] val = RedisProxy.this.serializeValue(value);
                            count = connection.lPush(keyByte, new byte[][]{val});
                        }

                        return count;
                    }
                });
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return 0L;
        }
    }

    public List<Object> lRange(final String key, final long begin, final long end) {
        if (this.cacheOpen) {
            try {
                return (List)this.redisTemplate.execute(new RedisCallback<List<Object>>() {
                    @Override
                    public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                        List<byte[]> retByteLst = connection.lRange(RedisProxy.this.serializeKey(key), begin, end);
                        if (null == retByteLst) {
                            return null;
                        } else {
                            List<Object> retValLst = new ArrayList(retByteLst.size());
                            Iterator var4 = retByteLst.iterator();

                            while(var4.hasNext()) {
                                byte[] retValByte = (byte[])var4.next();
                                Object val = RedisProxy.this.deserializeValue(retValByte);
                                retValLst.add(val);
                            }

                            return retValLst;
                        }
                    }
                });
            } catch (Exception var7) {
                var7.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Object lPop(final String key) {
        if (this.cacheOpen) {
            try {
                return this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        byte[] value = connection.lPop(RedisProxy.this.serializeKey(key));
                        return RedisProxy.this.deserializeValue(value);
                    }
                });
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Object rPop(final String key) {
        if (this.cacheOpen) {
            try {
                return this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        byte[] value = connection.rPop(RedisProxy.this.serializeKey(key));
                        return RedisProxy.this.deserializeValue(value);
                    }
                });
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Object clearRedis() {
        if (this.cacheOpen) {
            try {
                return this.redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        connection.flushDb();
                        return "ok";
                    }
                });
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getRedis() {
        if (!this.cacheOpen) {
            return null;
        } else {
            Set<String> keys = this.redisTemplate.keys("*");
            List<Map<String, Object>> list = new ArrayList();
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                HashMap<String, Object> map = new HashMap();
                Object value = this.redisTemplate.opsForValue().get(key);
                String string = JSONObject.toJSONString(value);
                map.put("key", key);
                map.put("value", string);
                list.add(map);
            }

            return list;
        }
    }

    public long cleanRedisAll() {
        if (this.cacheOpen) {
            Set<String> keys = this.redisTemplate.keys("*");
            return this.redisTemplate.delete(keys);
        } else {
            return 0L;
        }
    }

    public boolean cleanRedis(Map<String, Object> params) {
        return this.cacheOpen ? this.redisTemplate.delete(params.get("key").toString()) : false;
    }

    protected byte[] serializeKey(String key) {
        return this.redisTemplate.getStringSerializer().serialize(key);
    }

    protected byte[] serializeValue(Object value) {
        RedisSerializer<Object> reidsSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();
        return reidsSerializer.serialize(value);
    }

    protected Object deserializeValue(byte[] value) {
        return this.redisTemplate.getValueSerializer().deserialize(value);
    }

    public boolean getCacheOpen() {
        return this.cacheOpen;
    }

    public void setCacheOpen(boolean cacheOpen) {
        this.cacheOpen = cacheOpen;
    }
}
