package com.logistics.platform.delivery_service.deliveryRoute.domain.model;


import com.logistics.platform.delivery_service.delivery.domain.model.Delivery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "p_delivery_route")
public class DeliveryRoute {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id", nullable = false)
  private Delivery delivery;

  @Column(nullable = false)
  private UUID startHubId;

  @Column(nullable = false)
  private UUID endHubId;

  @Column(nullable = false)
  private UUID deliveryManagerId;

  @Column(nullable = false)
  private Double estimatedDuration;

  @Column(nullable = false)
  private Double estimatedDistance;

  @Column
  private Double actualDuration;

  @Column
  private Double actualDistance;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DeliveryRouteStatus status;

  @Column(nullable = false, length = 255)
  private Long sequence;

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
  public DeliveryRoute(Delivery delivery, UUID startHubId, UUID endHubId, UUID deliveryManagerId,
      Double estimatedDuration, Double estimatedDistance, Double actualDuration, Double actualDistance, DeliveryRouteStatus status, Long sequence, Boolean isDeleted, String createdBy) {
    this.delivery = delivery;
    this.startHubId = startHubId;
    this.endHubId = endHubId;
    this.deliveryManagerId = deliveryManagerId;
    this.estimatedDuration = estimatedDuration;
    this.estimatedDistance = estimatedDistance;
    this.actualDuration = actualDuration;
    this.actualDistance = actualDistance;
    this.status = status;
    this.sequence = sequence;
    this.isDeleted = isDeleted;
    this.createdBy = createdBy;
    this.createdAt = LocalDateTime.now();
  }

  public void updateActualMetrics(Double actualDuration, Double actualDistance) {
    this.actualDuration = actualDuration;
    this.actualDistance = actualDistance;
  }

  public void updateStatus(DeliveryRouteStatus status) {
    this.status = status;
  }

  public void deleteDeliveryRoute(String deletedBy) {
    this.status = DeliveryRouteStatus.DELETED;
    this.deletedAt = LocalDateTime.now();
  }

}