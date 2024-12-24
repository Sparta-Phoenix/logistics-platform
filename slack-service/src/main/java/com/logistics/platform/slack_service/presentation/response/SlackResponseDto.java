package com.logistics.platform.slack_service.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.logistics.platform.slack_service.domain.model.Slack;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlackResponseDto {

  private UUID slackId;

  private String senderSlackId;

  private String receiverSlackId;

  private String content;

  private String sendTs;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @QueryProjection
  public SlackResponseDto(Slack slack) {
    this.slackId = slack.getId();
    this.senderSlackId = slack.getSenderSlackId();
    this.receiverSlackId = slack.getReceiverSlackId();
    this.content = slack.getContent();
    this.sendTs = slack.getSendTs();
    this.createdAt = slack.getCreatedAt();
    this.updatedAt = slack.getUpdatedAt();
  }
}
