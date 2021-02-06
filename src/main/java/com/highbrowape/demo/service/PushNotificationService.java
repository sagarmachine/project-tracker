package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.PushNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PushNotificationService {



    @Autowired
     FcmService fcmService;

    public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", "msgid");
        pushData.put("text", "txt");
        pushData.put("user", "pankaj singh");
        return pushData;
    }
}
