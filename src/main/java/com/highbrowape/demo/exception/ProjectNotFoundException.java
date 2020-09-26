package com.highbrowape.demo.exception;

public class ProjectNotFoundException extends  RuntimeException{

    String message;

    public ProjectNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
