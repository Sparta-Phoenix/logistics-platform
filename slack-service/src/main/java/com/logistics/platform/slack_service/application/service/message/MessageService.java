package com.logistics.platform.slack_service.application.service.message;

public interface MessageService {

  String sendMessageToUser(String userSlackId, String message);

  void updateSendMessage(String channel, String ts, String newText);

  void deleteSendMessage(String channel, String ts);

}
