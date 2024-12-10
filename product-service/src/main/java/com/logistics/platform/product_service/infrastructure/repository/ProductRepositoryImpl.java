package com.logistics.platform.product_service.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ProductRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

}
