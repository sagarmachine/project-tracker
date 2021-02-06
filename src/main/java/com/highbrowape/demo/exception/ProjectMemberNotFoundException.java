package com.highbrowape.demo.exception;

public class ProjectMemberNotFoundException extends  RuntimeException{

    String message;

    public ProjectMemberNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
