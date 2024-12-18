package com.logistics.platform.product_service.application.service.company;

import com.logistics.platform.product_service.application.dto.CompanyResponse;
import java.util.UUID;

public interface CompanyService {

  CompanyResponse getCompanyInfo(UUID companyId, String userName, String userRole);
}
