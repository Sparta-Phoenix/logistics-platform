package com.logistics.platform.deliverymanagerservice.domain.repository;

import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryManager;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {



}
