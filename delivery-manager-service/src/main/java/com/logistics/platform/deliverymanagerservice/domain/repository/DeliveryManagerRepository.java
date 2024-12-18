package com.logistics.platform.deliverymanagerservice.domain.repository;

import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryManager;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryType;
import com.logistics.platform.deliverymanagerservice.domain.model.QDeliveryManager;
import com.logistics.platform.deliverymanagerservice.infrastructure.repository.DeliveryManagerRepositoryCustom;
import com.logistics.platform.deliverymanagerservice.presentation.response.DeliveryManagerResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import feign.Param;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>,
    DeliveryManagerRepositoryCustom, QuerydslPredicateExecutor<DeliveryManager>,
    QuerydslBinderCustomizer<QDeliveryManager> {

  Page<DeliveryManagerResponseDto> findAll(List<UUID> uuidList, Predicate predicate, Pageable pageable);

  Optional<DeliveryManager> findFirstByIsDeletedFalseAndDeliveryTypeAndDeliveryOrderNumber(
      DeliveryType deliveryType, Long deliveryOrderNumber);

  @Query("SELECT COALESCE(MAX(d.deliveryOrderNumber), 0) FROM DeliveryManager d WHERE d.deliveryType = :deliveryType AND d.isDeleted = false")
  Optional<Long> findMaxDeliveryOrderNumberByDeliveryType(@Param("deliveryType") DeliveryType deliveryType);

  @Query("SELECT COALESCE(MIN(d.deliveryOrderNumber), 0) FROM DeliveryManager d WHERE d.deliveryType = :deliveryType AND d.isDeleted = false")
  Optional<Long> findMinDeliveryOrderNumberByDeliveryType(@Param("deliveryType") DeliveryType deliveryType);

  boolean existsByUserId(Long userId);

  @Override // Predicate의 조건을 수정: 문자 검색 시 'equals 조건' -> 'contains 조건'
  default void customize(QuerydslBindings querydslBindings, @NotNull QDeliveryManager qDeliveryManager) {
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
