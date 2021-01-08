package com.lsc.content.distribution.facebook;

public class ContentFacebookException extends  RuntimeException{


    public ContentFacebookException(String message) {
        super(message);
    }

    public ContentFacebookException() {
    }

    public ContentFacebookException(String message, Throwable throwable) {
        super(message,throwable);
    }


}
