package com.logistics.platform.company_service.domain.model;

import com.logistics.platform.company_service.presentation.request.CompanyModifyRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_company")
public class Company {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID companyId;

  @Column(nullable = false)
  private UUID hubId;

  @Column(nullable = false)
  private Long companyManagerId;

  @Column(nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String roadAddress;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CompanyType companyType;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(updatable = false, nullable = false, length = 100)
  private String createdBy;

  @LastModifiedDate
  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(length = 100)
  private String updatedBy;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime deletedAt;

  @Column(length = 100)
  private String deletedBy;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  public void changeCompany(CompanyModifyRequest companyModifyRequest, String userName) {
    this.hubId = companyModifyRequest.getHubId();
    this.companyName = companyModifyRequest.getCompanyName();
    this.phoneNumber = companyModifyRequest.getPhoneNumber();
    this.roadAddress = companyModifyRequest.getRoadAddress();
        this.companyType =
        !companyModifyRequest.getIsCompanyTypeReceiver() ? CompanyType.MANUFACTURER
            : CompanyType.RECEIVER;
    this.updatedBy = userName;
  }

  public void deleteCompany(String userName) {
    this.isDeleted = true;
    this.deletedBy = userName;
  }
}
