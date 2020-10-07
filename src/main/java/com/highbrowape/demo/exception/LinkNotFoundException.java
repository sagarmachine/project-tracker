package com.highbrowape.demo.exception;

public class LinkNotFoundException extends  RuntimeException{

    String message;

    public LinkNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
