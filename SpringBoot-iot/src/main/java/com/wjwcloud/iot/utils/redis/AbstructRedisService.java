//package com.wjwcloud.iot.utils.redis;
//
//
//import com.wjwcloud.iot.model.AbstructBaseEntity;
//import com.wjwcloud.iot.model.AbstructBaseVo;
//import com.wjwcloud.iot.model.BaseService;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import javax.annotation.Resource;
//
//public abstract class AbstructRedisService<V, E> extends BaseService<V, E> implements IRedisService<V, E> {
//    @Resource(
//            name = "redisProxy"
//    )
//    protected RedisProxy redisProxy;
//    private boolean isCacheThread = true;
//
//    public AbstructRedisService() {
//    }
//
//    protected List<V> readCache(List<AbstructBaseEntity> entityList) throws Exception {
//        return this.readCache(entityList, 0L);
//    }
//
//    protected List<V> readCache(List<AbstructBaseEntity> entityList, long timeout) throws Exception {
//        List<V> result = null;
//        List<V> data = new ArrayList();
//        List<Object> neqIdList = new ArrayList();
//        Iterator var7 = entityList.iterator();
//
//        Object vo;
//        while(var7.hasNext()) {
//            AbstructBaseEntity entity = (AbstructBaseEntity)var7.next();
//            String key = entity.getTableName() + entity.getId();
//            vo = this.redisProxy.read(key);
//            if (vo != null) {
//                data.add(vo);
//            } else {
//                neqIdList.add(entity.getId());
//            }
//        }
//
//        if (neqIdList.isEmpty()) {
//            return data;
//        } else {
//            result = new ArrayList();
//            List<AbstructBaseEntity> list = this.findList4Ids(neqIdList);
//            Iterator var14 = list.iterator();
//
//            AbstructBaseEntity entity;
//            while(var14.hasNext()) {
//                entity = (AbstructBaseEntity)var14.next();
//                vo = this.assembleVo(entity);
//                String key = entity.getTableName() + entity.getId();
//                this.saveCache(key, vo, timeout);
//                data.add(vo);
//            }
//
//            var14 = entityList.iterator();
//
//            while(true) {
//                label46:
//                while(var14.hasNext()) {
//                    entity = (AbstructBaseEntity)var14.next();
//                    Iterator var16 = data.iterator();
//
//                    while(true) {
//                        while(true) {
//                            if (!var16.hasNext()) {
//                                continue label46;
//                            }
//
//                            V vo = var16.next();
//                            AbstructBaseVo avo = (AbstructBaseVo)vo;
//                            if (entity.getId() instanceof Long && avo.getId() instanceof Long) {
//                                if ((Long)entity.getId() == (Long)avo.getId()) {
//                                    result.add(vo);
//                                    continue label46;
//                                }
//                            } else if (entity.getId().toString().equals(avo.getId().toString())) {
//                                result.add(vo);
//                                continue label46;
//                            }
//                        }
//                    }
//                }
//
//                data = null;
//                neqIdList = null;
//                return result;
//            }
//        }
//    }
//
//    protected void saveCache(final String key, final V vo, final long timeout) {
//        if (this.isCacheThread) {
//            Thread t = new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        AbstructRedisService.this.redisProxy.save(key, vo, timeout);
//                    } catch (Exception var2) {
//                        var2.printStackTrace();
//                    }
//
//                }
//            });
//            t.start();
//        } else {
//            this.redisProxy.save(key, vo, timeout);
//        }
//
//    }
//
//    protected void saveCache(final String key, final V vo) {
//        if (this.isCacheThread) {
//            Thread t = new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        AbstructRedisService.this.redisProxy.save(key, vo);
//                    } catch (Exception var2) {
//                        var2.printStackTrace();
//                    }
//
//                }
//            });
//            t.start();
//        } else {
//            this.redisProxy.save(key, vo);
//        }
//
//    }
//
//    protected V readCache(String key) {
//        return this.redisProxy.read(key);
//    }
//
//    protected void deleteCache(final String key) {
//        if (this.isCacheThread) {
//            Thread t = new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        AbstructRedisService.this.redisProxy.delete(key);
//                    } catch (Exception var2) {
//                        var2.printStackTrace();
//                    }
//
//                }
//            });
//            t.start();
//        } else {
//            this.redisProxy.delete(key);
//        }
//
//    }
//
//    protected boolean getCacheOpen() {
//        return this.redisProxy.getCacheOpen();
//    }
//
//    public RedisProxy getRedisProxy() {
//        return this.redisProxy;
//    }
//
//    protected void clearCache() {
//        this.redisProxy.clearRedis();
//    }
//
//    @Override
//    public void setCacheThread(boolean isCacheThread) {
//        this.isCacheThread = isCacheThread;
//    }
//
//    protected abstract List<AbstructBaseEntity> findList4Ids(List<Object> var1) throws Exception;
//}
