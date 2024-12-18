package com.logistics.platform.delivery_service.deliveryRoute.domain.repository;

import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRoute;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.QDeliveryRoute;
import com.logistics.platform.delivery_service.deliveryRoute.infrastructure.repository.DeliveryRouteRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID>,
    DeliveryRouteRepositoryCustom, QuerydslPredicateExecutor<DeliveryRoute>,
    QuerydslBinderCustomizer<QDeliveryRoute> {

  @Override // Predicate의 조건을 수정: 문자 검색 시 'equals 조건' -> 'contains 조건'
  default void customize(QuerydslBindings querydslBindings, @NotNull QDeliveryRoute qDeliveryRoute) {
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
