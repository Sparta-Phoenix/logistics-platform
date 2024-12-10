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

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ProductRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<ProductResponseDto> findAll(
      List<UUID> uuidList, Predicate predicate, Pageable pageable) {

    QProduct product = QProduct.product;

    BooleanBuilder builder = new BooleanBuilder(predicate);
    if (uuidList != null && !uuidList.isEmpty()) {
      builder.and(product.id.in(uuidList));
    }
    builder.and(product.isDeleted.eq(false));

    int size = pageable.getPageSize();
    size = (size == 30 || size == 50) ? size : 10;
    pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

    List<ProductResponseDto> results = queryFactory
        .select(new QProductResponseDto(product))
        .from(product)
        .where(builder)
        .orderBy(
            getDynamicSort(
                pageable.getSort(), product.getType(), product.getMetadata())
                .toArray(OrderSpecifier[]::new))
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

  public static <T> List<OrderSpecifier> getDynamicSort(Sort sort, Class<? extends T> entityClass,
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

    return orderSpecifiers;
  }


}
