package com.logistics.platform.deliverymanagerservice.domain.repository;

import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryOrderIndex;
import com.logistics.platform.deliverymanagerservice.domain.model.DeliveryType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryOrderIndexRepository extends JpaRepository<DeliveryOrderIndex, Long> {

  Optional<DeliveryOrderIndex> findByDeliveryType(DeliveryType deliveryType);

}
