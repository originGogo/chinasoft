package com.gogo.psy.user.config;

import com.gogo.psy.common.Redis.IRedisProxy;
import com.gogo.psy.common.Redis.RedisProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;
    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.pool.minIdle}")
    private Integer minIdle;
    @Value("${redis.pool.host}")
    private String redisHost;
    @Value("${redis.pool.port}")
    private Integer redisPort;
    @Value("${redis.pool.redisTimeout}")
    private Integer redisTimeout;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        // 禁用MXBean注册
        poolConfig.setJmxEnabled(false);
        return poolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        return new JedisPool(jedisPoolConfig, redisHost, redisPort, redisTimeout);
    }

    @Bean
    public IRedisProxy buildRedis(){
        return new RedisProxy();
    }

}
