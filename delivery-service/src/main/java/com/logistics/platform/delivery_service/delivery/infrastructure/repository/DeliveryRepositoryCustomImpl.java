package com.logistics.platform.delivery_service.delivery.infrastructure.repository;



import static com.logistics.platform.delivery_service.deliveryRoute.domain.model.QDeliveryRoute.deliveryRoute;

import com.logistics.platform.delivery_service.delivery.domain.model.Delivery;
import com.logistics.platform.delivery_service.delivery.domain.model.QDelivery;
import com.logistics.platform.delivery_service.delivery.presentation.response.DeliveryResponseDto;
import com.logistics.platform.delivery_service.delivery.presentation.response.QDeliveryResponseDto;
import com.logistics.platform.delivery_service.deliveryRoute.presentation.response.DeliveryRouteResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  public DeliveryRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<DeliveryResponseDto> findAll(
      List<UUID> uuidList, Predicate predicate, Pageable pageable) {

    QDelivery delivery = QDelivery.delivery;

    BooleanBuilder builder = new BooleanBuilder(predicate); // predicate 적용
    if (uuidList != null && !uuidList.isEmpty()) { // idList 값이 있다면 조회
      builder.and(delivery.id.in(uuidList));
    }
    builder.and(delivery.isDeleted.eq(false)); // isDeleted=false 만 조회

    // size 10, 30, 50 이 아니라면 10으로 고정
    int size = pageable.getPageSize();
    size = (size == 30 || size == 50) ? size : 10;
    pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

    // 첫 번째 쿼리: 배송 조회
    List<Delivery> deliveries = queryFactory
        .selectFrom(delivery)
        .where(builder)
        .orderBy(getDynamicSort(pageable.getSort(), delivery.getType(), delivery.getMetadata()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 배송 ID 목록 생성
    List<UUID> deliveryIds = deliveries.stream()
        .map(Delivery::getId)
        .toList();

    // 두 번째 쿼리: 배송 경로 조회
    List<DeliveryRouteResponseDto> routes = queryFactory
        .select(Projections.constructor(
            DeliveryRouteResponseDto.class,
            deliveryRoute.id,
            deliveryRoute.startHubId,
            deliveryRoute.endHubId,
            deliveryRoute.deliveryManagerId,
            deliveryRoute.estimatedDuration,
            deliveryRoute.estimatedDistance,
            deliveryRoute.actualDuration,
            deliveryRoute.actualDistance,
            deliveryRoute.status,
            deliveryRoute.sequence
        ))
        .from(deliveryRoute)
        .where(deliveryRoute.delivery.id.in(deliveryIds))
        .fetch();

    // 배송 ID별 경로 매핑
    Map<UUID, List<DeliveryRouteResponseDto>> routeMap = routes.stream()
        .collect(Collectors.groupingBy(DeliveryRouteResponseDto::getDeliveryId));

    // 배송 DTO 생성
    List<DeliveryResponseDto> results = deliveries.stream()
        .map(d -> new DeliveryResponseDto(d, routeMap.getOrDefault(d.getId(), new ArrayList<>())))
        .collect(Collectors.toList());

    Long total = queryFactory
        .select(delivery.count())
        .from(delivery)
        .where(builder)
        .fetchOne();

    if (total == null) {
      total = 0L;
    }

    return new PageImpl<>(results, pageable, total);
  }

  // 정렬(Sort) 정보를 기반으로 Querydsl의 OrderSpecifier 객체들을 동적으로 생성
  // 제네릭을 사용하여 특정 엔터티 클래스에 종속되지 않음
  private <T> OrderSpecifier[] getDynamicSort(Sort sort, Class<? extends T> entityClass,
      PathMetadata pathMetadata) {
    List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

    // 도메인 클래스에 맞는 PathBuilder를 생성
    PathBuilder<Object> pathBuilder = new PathBuilder<>(entityClass, pathMetadata);

    sort.stream().forEach(orderSpecifier -> {
      Order direction = orderSpecifier.isAscending() ? Order.ASC : Order.DESC;
      String prop = orderSpecifier.getProperty();

      // 동적으로 해당 필드에 접근
      orderSpecifiers.add(new OrderSpecifier(direction, pathBuilder.get(prop)));
    });

    return orderSpecifiers.toArray(new OrderSpecifier[0]); // list 크기에 맞춰 배열 생성
  }

}
