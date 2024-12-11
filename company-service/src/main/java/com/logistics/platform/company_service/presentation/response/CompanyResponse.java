package com.logistics.platform.company_service.presentation.response;

import com.logistics.platform.company_service.domain.model.Company;
import com.logistics.platform.company_service.domain.model.CompanyType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CompanyResponse {

  private UUID companyId;
  private UUID hubId;

  private String companyName;

  private String number;

  private String address;

  private CompanyType checkCompanyType;

  public CompanyResponse(Company company) {
    this.companyId = company.getCompanyId();
    this.hubId = company.getHubId();
    this.companyName = company.getCompanyName();
    this.number = company.getNumber();
    this.address = company.getAddress();
    this.checkCompanyType = company.getCompanyType();
  }
}
