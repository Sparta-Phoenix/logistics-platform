package com.logistics.platform.delivery_service.delivery.domain.repository;

import com.logistics.platform.delivery_service.delivery.domain.model.Delivery;
import com.logistics.platform.delivery_service.delivery.domain.model.QDelivery;
import com.logistics.platform.delivery_service.delivery.infrastructure.repository.DeliveryRepositoryCustom;
import com.logistics.platform.delivery_service.delivery.presentation.response.DeliveryResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID>,
    DeliveryRepositoryCustom, QuerydslPredicateExecutor<Delivery>,
    QuerydslBinderCustomizer<QDelivery> {

  Page<DeliveryResponseDto> findAll(
      List<UUID> uuidList, Predicate predicate, Pageable pageable);

  @Override // Predicate의 조건을 수정: 문자 검색 시 'equals 조건' -> 'contains 조건'
  default void customize(QuerydslBindings querydslBindings, @NotNull QDelivery qDelivery) {
    querydslBindings.bind(String.class)
        .all((StringPath path, Collection<? extends String> values) -> {
          List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
          if (valueList.isEmpty()) {
            return Optional.empty();
          }
          BooleanBuilder booleanBuilder = new BooleanBuilder();
          for (String s : valueList) {
            booleanBuilder.or(path.containsIgnoreCase(s));
          }
          return Optional.of(booleanBuilder);
        });
  }

}
