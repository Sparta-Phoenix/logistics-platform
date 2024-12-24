package com.logistics.platform.slack_service.application.service.slack;

import com.logistics.platform.slack_service.application.service.auth.AuthService;
import com.logistics.platform.slack_service.application.service.message.MessageService;
import com.logistics.platform.slack_service.common.exception.CustomApiException;
import com.logistics.platform.slack_service.domain.model.Slack;
import com.logistics.platform.slack_service.domain.repository.SlackRepository;
import com.logistics.platform.slack_service.presentation.request.SlackRequestDto;
import com.logistics.platform.slack_service.presentation.response.SlackResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlackService {

  private final SlackRepository slackRepository;
  private final AuthService authService;
  private final MessageService messageService;

  public SlackResponseDto createMessage(SlackRequestDto slackRequestDto, String username) {

    String senderSlackId = authService.getSlackIdByUsername(username);

    String content = "출발 허브: " + slackRequestDto.getStartHubId() + "\n"
            + "도착 허브: " + slackRequestDto.getEndHubId() + "\n"
            + "예상 거리: " + slackRequestDto.getEstimatedDistance() + " km \n"
            + "예상 시간: " + slackRequestDto.getEstimatedDuration() + " h";

    String sendTs = messageService.sendMessageToUser(slackRequestDto.getDeliveryManagerSlackId(),
        content);

    Slack slack = new Slack(
        senderSlackId,
        slackRequestDto.getDeliveryManagerSlackId(),
        content,
        username,
        sendTs
    );

    slackRepository.save(slack);

    return new SlackResponseDto(slack);
  }

  @Transactional(readOnly = true)
  public SlackResponseDto getMessage(UUID slackId, String role) {

    if (!role.equals("ROLE_MASTER")) { // TODO Auth 서버에서 권한 검증
      throw new CustomApiException("Master 권한만 조회 할 수 있습니다.");
    }

    Slack slack = slackRepository.findById(slackId)
        .orElseThrow(() -> new CustomApiException("Check Slack ID. No exist."));

    return new SlackResponseDto(slack);
  }

  @Transactional(readOnly = true)
  public PagedModel<SlackResponseDto> getSlacksPage(List<UUID> uuidList, Predicate predicate,
      Pageable pageable,
      String role) {

    if (!role.equals("ROLE_MASTER")) { // TODO Auth 서버에서 권한 검증
      throw new CustomApiException("Master 권한만 조회 할 수 있습니다.");
    }

    Page<SlackResponseDto> slackResponseDtoPage = slackRepository.findAllToPage(uuidList, predicate,
        pageable);

    return new PagedModel<>(slackResponseDtoPage);
  }

  @Transactional
  public SlackResponseDto updateMessage(SlackRequestDto slackRequestDto, UUID slackId,
      String role, String userName) {

    if (!role.equals("ROLE_MASTER")) { // TODO Auth 서버에서 권한 검증
      throw new CustomApiException("Master 권한만 조회 할 수 있습니다.");
    }

    Slack slack = slackRepository.findById(slackId)
        .orElseThrow(() -> new CustomApiException("Check Slack ID. No exist."));

    slack.update(slackRequestDto, userName);

    messageService.updateSendMessage(slack.getReceiverSlackId(), slack.getSendTs(),
        slackRequestDto.getContent());

    return new SlackResponseDto(slack);
  }

  @Transactional
  public void deleteMessage(UUID slackId, String role, String userName) {

    if (!role.equals("ROLE_MASTER")) { // TODO Auth 서버에서 권한 검증
      throw new CustomApiException("Master 권한만 조회 할 수 있습니다.");
    }

    Slack slack = slackRepository.findById(slackId)
        .orElseThrow(() -> new CustomApiException("Check Slack ID. No exist."));

    messageService.deleteSendMessage(slack.getReceiverSlackId(), slack.getSendTs());

    slack.delete(userName);
  }
}
