package com.logistics.platform.slack_service.domain.model;

import com.logistics.platform.slack_service.presentation.request.SlackRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "p_slack")
public class Slack {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String senderSlackId;

  @Column(nullable = false)
  private String receiverSlackId;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private String sendTs; // slack 앱의 Timestamp

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  @CreatedBy
  private String createdBy;

  @LastModifiedBy
  private String updatedBy;

  private String deletedBy;

  @Column(nullable = false)
  private Boolean isDeleted = false;

  public Slack(String senderSlackId, String receiverSlackId, String content, String userName,
      String sendTs) {
    this.senderSlackId = senderSlackId;
    this.receiverSlackId = receiverSlackId;
    this.content = content;
    this.createdBy = userName;
    this.sendTs = sendTs;
  }

  public void update(SlackRequestDto slackRequestDto, String userName) {
    if (slackRequestDto.getDeliveryManagerSlackId() != null) {
      this.receiverSlackId = slackRequestDto.getDeliveryManagerSlackId();
    }
    if (slackRequestDto.getContent() != null) {
      this.content = slackRequestDto.getContent();
    }
    this.updatedBy = userName;
  }

  public void delete(String userName) {
    this.isDeleted = true;
    this.deletedBy = userName;
    this.deletedAt = LocalDateTime.now();
  }
}
