package com.logistics.platform.product_service.infrastructure.client;

import com.logistics.platform.product_service.application.dto.CompanyResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "company-service", path = "/api/companies")
public interface CompanyClient {

  @GetMapping("/{companyId}")
  CompanyResponse getCompanyInfo(
      @PathVariable UUID companyId,
      @RequestHeader(value = "X-User-Name") String userName,
      @RequestHeader(value = "X-User-Role") String role);

}
