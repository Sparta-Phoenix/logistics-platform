package com.logistics.platform.product_service.application.service.auth;

import com.logistics.platform.product_service.application.dto.UserResDto;
import javax.net.ssl.SSLSession;

public interface AuthService {

  UserResDto getUserInfo(String userName, String userRole);

  UserResDto getUserInfoById(Long userId, String userName, String userRole);
}
