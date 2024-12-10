package com.logistics.platform.product_service.domain.repository;

import com.logistics.platform.product_service.domain.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
