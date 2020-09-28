package com.highbrowape.demo.exception;

public class ProjectNoteNotFoundException extends  RuntimeException{

    String message;

    public ProjectNoteNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
