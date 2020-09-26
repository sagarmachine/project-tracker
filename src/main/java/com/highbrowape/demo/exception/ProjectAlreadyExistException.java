package com.highbrowape.demo.exception;

public class ProjectAlreadyExistException extends  RuntimeException{

    String message;

    public ProjectAlreadyExistException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
