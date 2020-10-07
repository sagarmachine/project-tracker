package com.highbrowape.demo.exception;

public class MissionMemberNotFoundException extends  RuntimeException{

    String message;

    public MissionMemberNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
