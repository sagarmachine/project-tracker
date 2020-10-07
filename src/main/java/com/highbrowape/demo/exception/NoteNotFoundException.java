package com.highbrowape.demo.exception;

public class NoteNotFoundException extends  RuntimeException{

    String message;

    public NoteNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
