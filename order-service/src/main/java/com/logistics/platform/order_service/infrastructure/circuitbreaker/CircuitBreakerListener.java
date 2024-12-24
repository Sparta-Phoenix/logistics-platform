package com.logistics.platform.order_service.infrastructure.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CircuitBreakerListener {

  private final CircuitBreakerRegistry circuitBreakerRegistry;

  @PostConstruct
  public void registerEventListener() {
    circuitBreakerRegistry.circuitBreaker("orderService").getEventPublisher()
        // 상태 전환 이벤트 리스너
        .onStateTransition(event -> log.info("#######CircuitBreaker State Transition: {}", event))
        // 실패율 초과 이벤트 리스너
        .onFailureRateExceeded(
            event -> log.warn("#######CircuitBreaker Failure Rate Exceeded: {}", event))
        // 호출 차단 이벤트 리스너
        .onCallNotPermitted(
            event -> log.warn("#######CircuitBreaker Call Not Permitted: {}", event))
        // 오류 발생 이벤트 리스너
        .onError(event -> log.warn("#######CircuitBreaker Error: {}", event));
  }


}
