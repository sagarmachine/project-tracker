package com.highbrowape.demo.exception;

public class ProjectLinkNotFoundException extends  RuntimeException{

    String message;

    public ProjectLinkNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
