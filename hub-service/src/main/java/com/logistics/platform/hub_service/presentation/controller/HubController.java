package com.logistics.platform.hub_service.presentation.controller;

import com.google.maps.errors.ApiException;
import com.logistics.platform.hub_service.application.service.HubService;
import com.logistics.platform.hub_service.presentation.global.ResponseDto;
import com.logistics.platform.hub_service.presentation.request.HubCreateRequest;
import com.logistics.platform.hub_service.presentation.request.HubModifyRequest;
import com.logistics.platform.hub_service.presentation.response.HubResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

  private final HubService hubService;

  @PostMapping
  public ResponseDto<HubResponse> create(@RequestBody @Valid HubCreateRequest hubCreateRequest,
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName)
      throws IOException, InterruptedException, ApiException {
    HubResponse hubResponse = hubService.createHub(hubCreateRequest, role, userName);
    return new ResponseDto<>(ResponseDto.SUCCESS, "허브가 생성되었습니다.", hubResponse);
  }

  @GetMapping("/{hubId}")
  public ResponseDto<HubResponse> get(@PathVariable UUID hubId,
      @RequestHeader(value = "X-User-Role") String role) {
    HubResponse hubResponse = hubService.getHub(hubId, role);
    return new ResponseDto<>(ResponseDto.SUCCESS, "허브 단건 조회가 완료되었습니다.", hubResponse);
  }

  @GetMapping
  public Page<HubResponse> search(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      Pageable pageable,
      @RequestHeader(value = "X-User-Role") String role) {
    return hubService.searchHubs(keyword, pageable, role);
  }

  @PatchMapping("/{hubId}")
  public ResponseDto<HubResponse> modify(@PathVariable UUID hubId,
      @RequestBody @Valid HubModifyRequest hubModifyRequest,
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName) {
    HubResponse hubResponse = hubService.modifyHub(hubId, hubModifyRequest, role, userName);
    return new ResponseDto<>(ResponseDto.SUCCESS, "허브 수정이 완료되었습니다.", hubResponse);
  }

  @PutMapping("/{hubId}")
  public ResponseDto<HubResponse> delete(@PathVariable UUID hubId,
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName) {
    hubService.deleteHub(hubId, role, userName);
    return new ResponseDto<>(ResponseDto.SUCCESS, "허브 삭제가 완료되었습니다.");
  }

  @GetMapping("/{hubManagerId}/hubIds")
  public ResponseDto<List<UUID>> getHubIds(@PathVariable Long hubManagerId) {
    List<UUID> hubIdByHubManagerIds = hubService.findHubIdByHubManagerId(hubManagerId);
    return new ResponseDto<>(ResponseDto.SUCCESS, "담당 허브Id 조회가 완료되었습니다.", hubIdByHubManagerIds);
  }

  // HubClient 용
  @GetMapping("/{hubId}/info")
  public HubResponse getByHubClient(@PathVariable UUID hubId) {
    return hubService.getHubInfo(hubId);
  }


}
