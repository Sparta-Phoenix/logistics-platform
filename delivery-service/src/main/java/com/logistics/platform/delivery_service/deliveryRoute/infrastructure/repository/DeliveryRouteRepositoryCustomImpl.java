package com.logistics.platform.delivery_service.deliveryRoute.infrastructure.repository;

import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRouteStatus;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.QDeliveryRoute;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.DeliveryRouteResponseDto;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.QDeliveryRouteResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class DeliveryRouteRepositoryCustomImpl implements DeliveryRouteRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  public DeliveryRouteRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<DeliveryRouteResponseDto> findAll(List<UUID> uuidList, Predicate predicate, Pageable pageable) {

    QDeliveryRoute deliveryRoute = QDeliveryRoute.deliveryRoute;

    BooleanBuilder builder = new BooleanBuilder(predicate); // predicate 적용
    if (uuidList != null && !uuidList.isEmpty()) { // idList 값이 있다면 조회
      builder.and(deliveryRoute.id.in(uuidList));
    }
    builder.and(deliveryRoute.isDeleted.eq(false)); // DELETED 상태는 제외

    // size 10, 30, 50 이 아니라면 10으로 고정
    int size = pageable.getPageSize();
    size = (size == 30 || size == 50) ? size : 10;
    pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

    List<DeliveryRouteResponseDto> results = queryFactory
        .select(new QDeliveryRouteResponseDto(deliveryRoute))
        .from(deliveryRoute)
        .where(builder)
        .orderBy(getDynamicSort(pageable.getSort(), deliveryRoute.getType(), deliveryRoute.getMetadata()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(deliveryRoute.count())
        .from(deliveryRoute)
        .where(builder)
        .fetchOne();

    if (total == null) {
      total = 0L;
    }

    return new PageImpl<>(results, pageable, total);
  }

  // 정렬(Sort) 정보를 기반으로 Querydsl의 OrderSpecifier 객체들을 동적으로 생성
  private <T> OrderSpecifier[] getDynamicSort(Sort sort, Class<? extends T> entityClass, PathMetadata pathMetadata) {
    List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

    // 도메인 클래스에 맞는 PathBuilder를 생성
    PathBuilder<Object> pathBuilder = new PathBuilder<>(entityClass, pathMetadata);

    sort.stream().forEach(orderSpecifier -> {
      Order direction = orderSpecifier.isAscending() ? Order.ASC : Order.DESC;
      String prop = orderSpecifier.getProperty();

      // 동적으로 해당 필드에 접근
      orderSpecifiers.add(new OrderSpecifier(direction, pathBuilder.get(prop)));
    });

    return orderSpecifiers.toArray(new OrderSpecifier[0]);
  }

}
