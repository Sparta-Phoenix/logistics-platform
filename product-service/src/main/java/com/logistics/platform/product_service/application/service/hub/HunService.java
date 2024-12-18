package com.logistics.platform.product_service.application.service.hub;

import java.util.UUID;

public interface HunService {

  UUID getHubIdByManagerId(Long id, String userName, String userRole);
}
