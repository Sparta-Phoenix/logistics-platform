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
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_delivery_manager")
public class DeliveryManager extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private UUID hubId;

  @Column(nullable = false)
  private UUID slackId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DeliveryType deliveryType;

  @Column(nullable = false)
  private Long deliveryOrderNumber;

  @Column(nullable = false)
  private Boolean isDeleted;

  @Builder
  public DeliveryManager(Long userId, UUID hubId, UUID slackId, DeliveryType deliveryType, Long deliveryOrderNumber, Boolean isDeleted){
    this.userId = userId;
    this.hubId = hubId;
    this.slackId = slackId;
    this.deliveryType = deliveryType;
    this.deliveryOrderNumber = deliveryOrderNumber;
    this.isDeleted = isDeleted;
  }

  public void updateDeliveryManager(DeliveryManagerRequestDto deliveryManagerRequestDto) {
    this.userId = deliveryManagerRequestDto.getUserId();
    this.hubId = deliveryManagerRequestDto.getHubId();
    this.slackId = deliveryManagerRequestDto.getSlackId();
    this.deliveryType = deliveryManagerRequestDto.getDeliveryType();
  }

  public void deleteDeliveryManager(String auditingUser) {
    this.isDeleted = true;
    super.delete(auditingUser);
  }


}
