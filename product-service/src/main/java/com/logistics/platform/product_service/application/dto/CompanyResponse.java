package com.logistics.platform.product_service.application.dto;

import com.logistics.platform.product_service.domain.model.CompanyType;
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

}
