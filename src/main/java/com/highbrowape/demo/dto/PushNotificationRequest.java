package com.highbrowape.demo.dto;


import lombok.Data;

@Data
public class PushNotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;
}
