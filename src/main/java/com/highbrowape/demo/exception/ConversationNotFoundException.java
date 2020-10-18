package com.highbrowape.demo.exception;

public class ConversationNotFoundException extends  RuntimeException{

    String message;

    public ConversationNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
