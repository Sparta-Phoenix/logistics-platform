package com.logistics.platform.product_service.domain.repository;

import com.logistics.platform.product_service.domain.model.Product;
import com.logistics.platform.product_service.domain.model.QProduct;
import com.logistics.platform.product_service.infrastructure.repository.ProductRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ProductRepository extends JpaRepository<Product, UUID>,
    ProductRepositoryCustom,
    QuerydslPredicateExecutor<Product>,
    QuerydslBinderCustomizer<QProduct> {

  @Override
  default void customize(QuerydslBindings querydslBindings, @NotNull QProduct qProduct) {
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
