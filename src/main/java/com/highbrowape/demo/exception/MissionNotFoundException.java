package com.highbrowape.demo.exception;

public class MissionNotFoundException extends  RuntimeException{

    String message;

    public MissionNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
