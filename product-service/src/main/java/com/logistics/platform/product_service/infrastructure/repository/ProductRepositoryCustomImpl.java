package com.logistics.platform.product_service.infrastructure.repository;

import com.logistics.platform.product_service.domain.model.QProduct;
import com.logistics.platform.product_service.presentation.response.ProductResponseDto;
import com.logistics.platform.product_service.presentation.response.QProductResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    pageable = Pageable.ofSize(size).withPage(pageable.getPageNumber());

    List<ProductResponseDto> results = queryFactory
        .select(new QProductResponseDto(product))
        .from(product)
        .where(builder)
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


}
