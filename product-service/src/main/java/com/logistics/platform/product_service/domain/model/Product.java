package com.logistics.platform.product_service.domain.model;

import com.logistics.platform.product_service.presentation.request.ProductRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String productName;

  @Column(nullable = false)
  private Long price;

  @Column
  private Long count = 0L;

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
  private LocalDateTime deletedAt;

  @Column
  private String deletedBy;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  // 업체 ID
  @Column(nullable = false)
  private UUID companyId;

  // 관리허브 ID
  @Column(nullable = false)
  private UUID hubId;

  @Builder
  public Product(
      String productName, Long price, Long count, String createdBy, UUID companyId, UUID hubId
  ) {
    this.productName = productName;
    this.price = price;
    if (count != null) {
      this.count = count;
    }
    this.createdBy = createdBy;
    this.companyId = companyId;
    this.hubId = hubId;
  }


  public void update(ProductRequestDto productRequestDto) {
    if (productRequestDto.getProductName() != null) {
      this.productName = productRequestDto.getProductName();
    }
    if (productRequestDto.getPrice() != null) {
      this.price = productRequestDto.getPrice();
    }
    if (productRequestDto.getCount() != null) {
      this.count = productRequestDto.getCount();
    }
    if (productRequestDto.getCompanyId() != null) {
      this.companyId = productRequestDto.getCompanyId();
    }
    if (productRequestDto.getHubId() != null) {
      this.hubId = productRequestDto.getHubId();
    }
    // 수정자 추가

  }

  public void delete() {
    this.isDeleted = true;
    // 삭제자 추가
  }
}
