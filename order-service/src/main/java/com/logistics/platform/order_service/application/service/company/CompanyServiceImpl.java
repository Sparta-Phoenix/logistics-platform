package com.logistics.platform.order_service.application.service.company;

import com.logistics.platform.order_service.infrastructure.client.CompanyClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

  private final CompanyClient companyClient;

  @Override
  public UUID getCompanyHubId(UUID companyId) {
    return companyClient.getCompanyHubId(companyId);
  }
}
