package com.logistics.platform.order_service.infrastructure.repository;

import com.logistics.platform.order_service.presentation.response.OrderResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

  Page<OrderResponseDto> findAllToPage(List<UUID> uuidList, Predicate predicate, Pageable pageable,
      String userName);

}
