package com.logistics.platform.deliverymanagerservice.application.service;

import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryOrderIndex;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryType;
import com.logistics.platform.deliverymanagerservice.domain.repository.DeliveryOrderIndexRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerOrderIndexService {

  private final DeliveryOrderIndexRepository repository;

  public ManagerOrderIndexService(DeliveryOrderIndexRepository repository) {
    this.repository = repository;
  }

  // 현재 배송 순번 인덱스 가져오기
  @Transactional(readOnly = true)
  public Long getCurrentOrderIndex(DeliveryType deliveryType) {
    return repository.findByDeliveryType(deliveryType)
        .map(DeliveryOrderIndex::getCurrentOrderIndex)
        .orElse(1L);
  }

  // 다음 배송 순번 인덱스 업데이트
  @Transactional
  public void updateCurrentOrderIndex(DeliveryType deliveryType, Long nextOrderIndex) {
    DeliveryOrderIndex index = repository.findByDeliveryType(deliveryType)
        .orElse(new DeliveryOrderIndex(deliveryType, 1L));

    index.setCurrentOrderIndex(nextOrderIndex);
    repository.save(index);
  }

}
