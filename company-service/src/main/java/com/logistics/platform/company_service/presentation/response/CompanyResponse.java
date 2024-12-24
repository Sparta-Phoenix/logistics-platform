package com.logistics.platform.company_service.presentation.response;

import com.logistics.platform.company_service.domain.model.Company;
import com.logistics.platform.company_service.domain.model.CompanyType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CompanyResponse {

  private UUID companyId;

  private UUID hubId;

  private Long companyManagerId;

  private String companyName;

  private String phoneNumber;

  private String address;

  private CompanyType checkCompanyType;

  public CompanyResponse(Company company) {
    this.companyId = company.getCompanyId();
    this.hubId = company.getHubId();
    this.companyManagerId = company.getCompanyManagerId();
    this.companyName = company.getCompanyName();
    this.phoneNumber = company.getPhoneNumber();
    this.address = company.getRoadAddress();
    this.checkCompanyType = company.getCompanyType();
  }
}
