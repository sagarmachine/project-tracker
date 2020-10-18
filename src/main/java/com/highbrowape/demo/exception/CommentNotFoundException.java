package com.highbrowape.demo.exception;

public class CommentNotFoundException extends  RuntimeException{

    String message;

    public CommentNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
