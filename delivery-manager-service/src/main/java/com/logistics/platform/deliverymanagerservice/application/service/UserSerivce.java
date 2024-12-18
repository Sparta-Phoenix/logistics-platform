package com.logistics.platform.deliverymanagerservice.application.service;

import com.logistics.platform.deliverymanagerservice.application.dto.UserResDto;

public interface UserSerivce {

  UserResDto getUserInfo(String userName, String userRole);

}
