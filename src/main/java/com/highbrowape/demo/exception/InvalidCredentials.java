package com.highbrowape.demo.exception;

public class InvalidCredentials extends  RuntimeException{

    String message;

    public InvalidCredentials(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
