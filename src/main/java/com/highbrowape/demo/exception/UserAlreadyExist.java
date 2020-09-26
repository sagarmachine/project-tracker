package com.highbrowape.demo.exception;

public class UserAlreadyExist extends  RuntimeException{

    String message;

    public UserAlreadyExist(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
