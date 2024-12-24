package com.logistics.platform.order_service.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", path = "/api/companies")
public interface CompanyClient {

  @GetMapping("/{companyId}/hub")
  UUID getCompanyHubId(@PathVariable UUID companyId);

}
