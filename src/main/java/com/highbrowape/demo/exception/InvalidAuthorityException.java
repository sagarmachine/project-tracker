package com.highbrowape.demo.exception;

public class InvalidAuthorityException extends  RuntimeException{

    String message;

    public InvalidAuthorityException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
