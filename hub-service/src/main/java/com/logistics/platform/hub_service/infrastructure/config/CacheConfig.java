package com.logistics.platform.hub_service.infrastructure.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean // Redis Cache 기본값 설정
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration configuration = RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(10))
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
        );
    return RedisCacheManager
        .builder(connectionFactory)
        .cacheDefaults(configuration)
        .build();

  }
}
