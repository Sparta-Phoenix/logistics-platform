package com.logistics.platform.slack_service.presentation.controller;

import com.logistics.platform.slack_service.application.service.message.MessageService;
import com.logistics.platform.slack_service.application.service.slack.SlackService;
import com.logistics.platform.slack_service.common.ResponseDto;
import com.logistics.platform.slack_service.domain.model.Slack;
import com.logistics.platform.slack_service.presentation.request.SlackRequestDto;
import com.logistics.platform.slack_service.presentation.response.SlackResponseDto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slacks")
public class SalckController {

  private final SlackService slackService;
  private final MessageService messageService;

  @PostMapping
  public ResponseDto<SlackResponseDto> createMessage(
      @RequestBody SlackRequestDto slackRequestDto,
      @RequestHeader(value = "X-User-Name") String userName
  ) {

    SlackResponseDto slackResponseDto = slackService.createMessage(slackRequestDto, userName);

    return new ResponseDto<>(ResponseDto.SUCCESS, "메제지가 발송되었습니다.", slackResponseDto);
  }

  @GetMapping("/{slackId}")
  public ResponseDto<SlackResponseDto> getMessage(
      @PathVariable UUID slackId,
      @RequestHeader(value = "X-User-Role") String role
  ) {

    SlackResponseDto slackResponseDto = slackService.getMessage(slackId, role);

    return new ResponseDto<>(ResponseDto.SUCCESS, "메세지가 조회되었습니다", slackResponseDto);
  }

  @GetMapping
  public ResponseDto<PagedModel<?>> getMessagesPage(
      @RequestParam(required = false) List<UUID> uuidList,
      @QuerydslPredicate(root = Slack.class) Predicate predicate,
      @PageableDefault(direction = Direction.DESC, sort = "createdAt") Pageable pageable,
      @RequestHeader(value = "X-User-Role") String role
  ) {

    PagedModel<SlackResponseDto> slackResponseDtoPage
        = slackService.getSlacksPage(uuidList, predicate, pageable, role);

    return new ResponseDto<>(ResponseDto.SUCCESS, "메세지 목록이 조회되었습니다.", slackResponseDtoPage);
  }

  @PatchMapping("/{slackId}")
  public ResponseDto<SlackResponseDto> updateMessage(
      @RequestBody SlackRequestDto slackRequestDto,
      @PathVariable UUID slackId,
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName
  ) {

    SlackResponseDto slackResponseDto = slackService.updateMessage(slackRequestDto, slackId, role,
        userName);

    return new ResponseDto<>(ResponseDto.SUCCESS, "메세지가 수정되었습니다.", slackResponseDto);
  }

  @DeleteMapping("/{slackId}")
  public ResponseDto<SlackResponseDto> deleteMessage(
      @PathVariable UUID slackId,
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName
  ) {

    slackService.deleteMessage(slackId, role, userName);

    return new ResponseDto<>(ResponseDto.SUCCESS, "메세지가 삭제되었습니다.");
  }


  @PostMapping("/send")
  public void sendMessage(@RequestParam String userSlackId, @RequestParam String message) {
    messageService.sendMessageToUser(userSlackId, message);
  }

  @PostMapping("/deliveryManager")
  public void createMessageToDeliveryManager(
      @RequestBody SlackRequestDto slackRequestDto,
      @RequestHeader(value = "X-User-Name") String userName
  ) {
    slackService.createMessage(slackRequestDto, userName);
  }

}
