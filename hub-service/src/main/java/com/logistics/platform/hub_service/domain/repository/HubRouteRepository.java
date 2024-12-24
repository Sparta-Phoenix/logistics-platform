package com.logistics.platform.hub_service.domain.repository;

import com.logistics.platform.hub_service.domain.model.HubRoute;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {

}
