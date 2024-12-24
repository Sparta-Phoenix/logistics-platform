package com.logistics.platform.product_service.application.service.company;

import com.logistics.platform.product_service.application.dto.CompanyResponse;
import com.logistics.platform.product_service.infrastructure.client.CompanyClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

  private final CompanyClient companyClient;

  @Override
  public CompanyResponse getCompanyInfo(UUID companyId, String userName, String userRole) {
    return companyClient.getCompanyInfo(companyId, userName, userRole);
  }
}
