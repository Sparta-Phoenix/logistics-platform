package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.application.dto.HubResponseDto;
import java.util.UUID;

public interface HubService {

  HubResponseDto getHubDtoByHubId(UUID hubId);
}
