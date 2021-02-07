package com.highbrowape.demo.exception;

public class MeetingNotFoundException extends  RuntimeException{

    String message;

    public MeetingNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
