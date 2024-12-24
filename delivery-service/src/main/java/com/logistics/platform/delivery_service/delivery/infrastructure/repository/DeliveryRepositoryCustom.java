package com.logistics.platform.delivery_service.delivery.infrastructure.repository;

import com.logistics.platform.delivery_service.delivery.presentation.response.DeliveryResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepositoryCustom {

  Page<DeliveryResponseDto> findAll(
      List<UUID> uuidList, Predicate predicate, Pageable pageable);

}
