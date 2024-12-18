package com.logistics.platform.order_service.domain.model;

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
@Table(name = "p_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private UUID productId;

  @Column(nullable = false)
  private UUID supplyCompayId;

  @Column(nullable = false)
  private UUID receiveCompanyId;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private Long productQuantity;

  @Column(nullable = false)
  private Long totalPrice;

  @Column
  private String orderRequest;

  /**
   * 공통 부분 - 추후 상속으로 구현
   */
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

  @Column
  private String deletedBy;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  @Builder
  public Order(
      UUID productId,
      UUID supplyCompayId,
      UUID receiveCompanyId,
      Long productQuantity,
      Long totalPrice,
      String orderRequest,
      String createdBy,
      String address
  ) {
    this.productId = productId;
    this.supplyCompayId = supplyCompayId;
    this.receiveCompanyId = receiveCompanyId;
    this.productQuantity = productQuantity;
    this.totalPrice = totalPrice;
    this.orderRequest = orderRequest;
    this.createdBy = createdBy;
    this.address = address;
  }

  public void update(UUID productId, UUID supplyCompanyId, UUID receiveCompanyId,
      Long productQuantity, String orderRequest, Long totalPrice, String userName, String address) {
    if (productId != null) {
      this.productId = productId;
    }
    if (supplyCompanyId != null) {
      this.supplyCompayId = supplyCompanyId;
    }
    if (receiveCompanyId != null) {
      this.receiveCompanyId = receiveCompanyId;
    }
    if (productQuantity != null) {
      this.productQuantity = productQuantity;
    }
    if (orderRequest != null) {
      this.orderRequest = orderRequest;
    }
    if (totalPrice != null) {
      this.totalPrice = totalPrice;
    }
    if (address != null) {
      this.address = address;
    }
    this.updatedBy = userName;
  }

  public void delete(String userName) {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = userName;
  }
}
