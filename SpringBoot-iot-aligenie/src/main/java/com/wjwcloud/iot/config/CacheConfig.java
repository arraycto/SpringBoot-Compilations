package com.wjwcloud.iot.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * cacheManager名称
     */
    public interface CacheManagerName {
        /**
         * redis
         */
        String REDIS_CACHE_MANAGER = "redisCacheManager";

        /**
         * ehCache
         */
        String EHCACHE_CACHE_MAANGER = "ehCacheCacheManager";
    }
    /**
     *  定义 StringRedisTemplate ，指定序列号和反序列化的处理类
     * @param
     * @return
     */
//    @bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//        StringRedisTemplate template = new StringRedisTemplate(factory);
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
//                Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        //序列化 值时使用此序列化方法
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }

//    @bean(CacheConfig.CacheManagerName.REDIS_CACHE_MANAGER)
//    @Primary
//    public RedisCacheManager redisCacheManager(RedisTemplate<String,String> redisTemplate) {
//        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//        //使用前缀
//        rcm.setUsePrefix(true);
//        //缓存分割符 默认为 ":"
////        rcm.setCachePrefix(new DefaultRedisCachePrefix(":"));
//        //设置缓存过期时间
//        //rcm.setDefaultExpiration(60);//秒
//        return rcm;
//    }

    @Bean(CacheConfig.CacheManagerName.EHCACHE_CACHE_MAANGER)
    public EhCacheCacheManager EhcacheManager() {
        EhCacheCacheManager ehCacheManager = new EhCacheCacheManager();
        return ehCacheManager;
    }
}