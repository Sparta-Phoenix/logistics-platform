package com.logistics.platform.company_service.infrastructure.config;

import com.logistics.platform.company_service.application.dto.HubResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, HubResponseDto> hubResponseRedisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, HubResponseDto> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(RedisSerializer.string());
    template.setValueSerializer(RedisSerializer.json());
    return template;
  }
}
