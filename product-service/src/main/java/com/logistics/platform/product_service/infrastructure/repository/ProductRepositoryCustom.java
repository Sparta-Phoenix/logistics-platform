package com.logistics.platform.product_service.infrastructure.repository;

import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

  Page<ProductResponseDto> findAllByHubManager(List<UUID> uuidList, Predicate predicate, Pageable pageable,
      UUID hubIdByManagerId);

  Page<ProductResponseDto> findAll(List<UUID> uuidList, Predicate predicate, Pageable pageable);
}
