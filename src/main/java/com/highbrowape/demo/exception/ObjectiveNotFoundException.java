package com.highbrowape.demo.exception;

public class ObjectiveNotFoundException extends  RuntimeException{

    String message;

    public ObjectiveNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
