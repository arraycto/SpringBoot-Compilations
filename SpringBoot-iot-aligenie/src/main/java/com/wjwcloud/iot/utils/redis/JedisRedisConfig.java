package com.wjwcloud.iot.utils.redis;



import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource({"classpath:redis-${spring.profiles.active}.properties"})
public class JedisRedisConfig {
    @Value("${redis.hostName}")
    private String hostName;
    @Value("${redis.port}")
    private Integer port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.timeout}")
    private Integer timeout;
    @Value("${redis.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;
    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;
    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn}")
    private boolean testOnReturn;
    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;
    @Value("${spring.redis.cluster.max-redirects}")
    private Integer mmaxRedirectsac;
    @Value("${redis.start}")
    private Boolean cacheOpen;
    private int database = 0;
    private int soTimeout = 2000;
    private String clientName;
    private boolean ssl;
    private SSLSocketFactory sslSocketFactory;
    private SSLParameters sslParameters;
    private HostnameVerifier hostnameVerifier;

    public JedisRedisConfig() {
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMaxTotal(this.maxTotal);
        jedisPoolConfig.setMaxWaitMillis((long)this.maxWaitMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis((long)this.minEvictableIdleTimeMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(this.numTestsPerEvictionRun);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setTestOnBorrow(this.testOnBorrow);
        jedisPoolConfig.setTestOnReturn(this.testOnReturn);
        jedisPoolConfig.setTestWhileIdle(this.testWhileIdle);
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(this.hostName);
        jedisConnectionFactory.setPort(this.port);
        jedisConnectionFactory.setPassword(this.password);
        jedisConnectionFactory.setTimeout(this.timeout);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        this.initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setConnectionFactory(factory);
    }

    @Bean(
            name = {"redisProxy"}
    )
    public RedisProxy redisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisProxy redisProxy = new RedisProxy();
        redisProxy.setRedisTemplate(redisTemplate);
        redisProxy.setCacheOpen(this.cacheOpen);
        return redisProxy;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxIdle() {
        return this.maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxTotal() {
        return this.maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxWaitMillis() {
        return this.maxWaitMillis;
    }

    public void setMaxWaitMillis(Integer maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Integer getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getClusterNodes() {
        return this.clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public Integer getMmaxRedirectsac() {
        return this.mmaxRedirectsac;
    }

    public void setMmaxRedirectsac(Integer mmaxRedirectsac) {
        this.mmaxRedirectsac = mmaxRedirectsac;
    }

    public Boolean getCacheOpen() {
        return this.cacheOpen;
    }

    public void setCacheOpen(Boolean cacheOpen) {
        this.cacheOpen = cacheOpen;
    }

    public int getDatabase() {
        return this.database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        if ("".equals(clientName)) {
            clientName = null;
        }

        this.clientName = clientName;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public SSLParameters getSslParameters() {
        return this.sslParameters;
    }

    public void setSslParameters(SSLParameters sslParameters) {
        this.sslParameters = sslParameters;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }
}
