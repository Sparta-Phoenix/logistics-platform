package com.logistics.platform.company_service.domain.model;

import com.logistics.platform.company_service.presentation.request.CompanyModifyRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_company")
public class Company {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID companyId;

  @Column(nullable = false)
  private UUID hubId;

  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String number;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CompanyType companyType;

  // todo 생성일, ~ 삭제자 어떻게 추가할건지?

  @Column(nullable = false)
  private boolean isDeleted;

  public void changeCompany(CompanyModifyRequest companyModifyRequest) {
    this.hubId = companyModifyRequest.getHubId();
    this.companyName = companyModifyRequest.getCompanyName();
    this.number = companyModifyRequest.getNumber();
    this.address = companyModifyRequest.getAddress();
    this.companyType = !companyModifyRequest.isCheckCompanyType() ? CompanyType.MANUFACTURER
        : CompanyType.RECEIVER;
  }

  public void deleteCompany() {
    this.isDeleted = true;
  }
}
