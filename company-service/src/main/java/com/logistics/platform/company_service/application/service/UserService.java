package com.logistics.platform.company_service.application.service;

import com.logistics.platform.company_service.application.dto.UserResDto;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {

  UserResDto getUserId(
      @RequestHeader(value = "X-User-Role") String role,
      @RequestHeader(value = "X-User-Name") String userName);
}
