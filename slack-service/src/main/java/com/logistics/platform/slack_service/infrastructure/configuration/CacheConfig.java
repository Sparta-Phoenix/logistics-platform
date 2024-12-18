package com.logistics.platform.slack_service.infrastructure.configuration;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheManager cacheManager(
      RedisConnectionFactory redisConnectionFactory
  ) {

    return RedisCacheManager
        .builder(redisConnectionFactory)
//        .cacheDefaults(configuration)  // 기본 설정 제외
        .withCacheConfiguration("getSlackId", getCacheConfiguration(String.class))
        .build();
  }

  private <T> RedisCacheConfiguration getCacheConfiguration(Class<T> valueType) {
    return RedisCacheConfiguration
        .defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(60))  // 기본 캐시 유지 시간
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new Jackson2JsonRedisSerializer<>(valueType)
            )
        );
  }


}