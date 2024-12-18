package com.logistics.platform.deliverymanagerservice.domain.model;

import com.logistics.platform.deliverymanagerservice.presentation.request.DeliveryManagerRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Setter;
import org.hibernate.annotations.Comment;
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
@Table(name = "p_delivery_manager")
public class DeliveryManager {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private Long userId;

  @Column
  private UUID hubId;

  @Column(nullable = false)
  private String slackId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DeliveryType deliveryType;

  @Setter
  @Column(nullable = false)
  private Long deliveryOrderNumber;

  @Column(nullable = false)
  private Boolean isDeleted = false;  // Boolean타입 기본값 null이라 false로 지정해주기

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


  @Builder
  public DeliveryManager(Long userId, UUID hubId, String slackId, DeliveryType deliveryType, Long deliveryOrderNumber, Boolean isDeleted, String createdBy){
    this.userId = userId;
    this.hubId = hubId;
    this.slackId = slackId;
    this.deliveryType = deliveryType;
    this.deliveryOrderNumber = deliveryOrderNumber;
    this.isDeleted = isDeleted;
    this.createdBy = createdBy;
    this.createdAt = LocalDateTime.now();
  }

  public void updateDeliveryManager(DeliveryManagerRequestDto deliveryManagerRequestDto) {
    this.userId = deliveryManagerRequestDto.getUserId();
    this.hubId = deliveryManagerRequestDto.getHubId();
    this.slackId = deliveryManagerRequestDto.getSlackId();
    this.deliveryType = deliveryManagerRequestDto.getDeliveryType();
  }

  public void deleteDeliveryManager() {
    this.isDeleted = true;
    this.deletedAt = LocalDateTime.now();
  }

//  public void setDeliveryOrderNumber(Long deliveryOrderNumber) {
//    this.deliveryOrderNumber = deliveryOrderNumber;
//  } // @Setter 사용


}
