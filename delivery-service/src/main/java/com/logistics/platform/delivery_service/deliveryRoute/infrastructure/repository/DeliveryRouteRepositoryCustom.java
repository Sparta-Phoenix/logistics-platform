package com.logistics.platform.delivery_service.deliveryRoute.infrastructure.repository;

import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.DeliveryRouteResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRouteRepositoryCustom {

  Page<DeliveryRouteResponseDto> findAll(
      List<UUID> uuidList, Predicate predicate, Pageable pageable);

}
