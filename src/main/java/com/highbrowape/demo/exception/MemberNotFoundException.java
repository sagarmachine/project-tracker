package com.highbrowape.demo.exception;

public class MemberNotFoundException extends  RuntimeException{

    String message;

    public MemberNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
