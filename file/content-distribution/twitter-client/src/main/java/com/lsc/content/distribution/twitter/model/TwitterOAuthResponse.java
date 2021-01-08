package com.lsc.content.distribution.twitter.model;

public class TwitterOAuthResponse {


    private String accessToken;
    private String accessSecret;

    public TwitterOAuthResponse(String accessToken, String accessSecret) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}
