package com.logistics.platform.delivery_service.delivery.domain.model;


import static com.logistics.platform.delivery_service.delivery.domain.model.DeliveryStatus.WAITING_AT_HUB;

import com.logistics.platform.delivery_service.delivery.presentation.request.DeliveryRequestDto;
import com.logistics.platform.delivery_service.delivery.presentation.request.DeliveryUpdateRequestDto;
import com.logistics.platform.delivery_service.deliveryRoute.domain.model.DeliveryRoute;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "p_delivery")
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private UUID startHubId;

  @Column(nullable = false)
  private UUID endHubId;

  @Column(nullable = false)
  private UUID orderId;

  @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DeliveryStatus deliveryStatus = WAITING_AT_HUB;

  @Column(nullable = false, length = 100)
  private String recipient;

  @Column(nullable = false, length = 100)
  private String recipientSlackId;

  @Column(nullable = false, length = 255)
  private String address;

  @Column(nullable = false)
  private Boolean isDeleted = false;

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
  public Delivery(Long userId, UUID startHubId, UUID endHubId, UUID orderId, DeliveryStatus deliveryStatus, String recipient, String recipientSlackId, String address, Boolean isDeleted , String createdBy) {
    this.userId = userId;
    this.startHubId = startHubId;
    this.endHubId = endHubId;
    this.orderId = orderId;
    this.deliveryStatus = deliveryStatus;
    this.recipient = recipient;
    this.recipientSlackId = recipientSlackId;
    this.address = address;
    this.isDeleted = isDeleted;
    this.createdBy = createdBy;
    this.createdAt = LocalDateTime.now();
  }

  public void updateDelivery(DeliveryUpdateRequestDto deliveryUpdateRequestDto) {
    this.startHubId = deliveryUpdateRequestDto.getStartHubId();
    this.endHubId = deliveryUpdateRequestDto.getEndHubId();
    this.orderId = deliveryUpdateRequestDto.getOrderId();
    this.deliveryStatus = deliveryUpdateRequestDto.getDeliveryStatus();
    this.recipient = deliveryUpdateRequestDto.getRecipient();
    this.recipientSlackId = deliveryUpdateRequestDto.getRecipientSlackId();
    this.address = deliveryUpdateRequestDto.getAddress();
  }

  // 배송수정으로 상태 업데이트 말고 배송완료 메서드가 따로 필요하지 않을까 해서 만들어 본 것
  public void completeDelivery(String completedBy) {
    this.deliveryStatus = DeliveryStatus.DELIVERY_COMPLETED;
  }


  // 배송삭제와 배송취소가 분리되어서 필요할 것으로 보여서 만들어 둠
  public void cancelDelivery(String cancelledBy) {
    this.deliveryStatus = DeliveryStatus.DELIVERY_CANCELLED;
  }


  public void deleteDelivery(String deletedBy) {
    this.deliveryStatus = DeliveryStatus.DELIVERY_DELETED;
    this.deletedAt = LocalDateTime.now();
  }

  // 배송 경로 추가 메서드
  public void addDeliveryRoute(DeliveryRoute deliveryRoute) {
    this.deliveryRoutes.add(deliveryRoute);
    deliveryRoute.setDelivery(this);
  }

}
