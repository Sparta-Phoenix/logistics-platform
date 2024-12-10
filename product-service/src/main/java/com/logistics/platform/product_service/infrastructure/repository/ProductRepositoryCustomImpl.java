package com.logistics.platform.product_service.infrastructure.repository;

import com.logistics.platform.product_service.domain.model.QProduct;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import com.logistics.platform.product_service.presentation.response.QProductResponseDto;
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

// ProductRepositoryImpl로 구현시 메서드명 에러: ProductRepositoryCustomImpl로 변경
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ProductRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<ProductResponseDto> findAll(
      List<UUID> uuidList, Predicate predicate, Pageable pageable) {

    QProduct product = QProduct.product;

    BooleanBuilder builder = new BooleanBuilder(predicate); // predicate 적용
    if (uuidList != null && !uuidList.isEmpty()) { // idList 값이 있다면 조회
      builder.and(product.id.in(uuidList));
    }
    builder.and(product.isDeleted.eq(false)); // isDeleted=false 만 조회

    // size 10, 30, 50 이 아니라면 10으로 고정
    int size = pageable.getPageSize();
    size = (size == 30 || size == 50) ? size : 10;
    pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

    List<ProductResponseDto> results = queryFactory
        .select(new QProductResponseDto(product))
        .from(product)
        .where(builder)
        .orderBy(getDynamicSort(pageable.getSort(), product.getType(), product.getMetadata()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(product.count())
        .from(product)
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
