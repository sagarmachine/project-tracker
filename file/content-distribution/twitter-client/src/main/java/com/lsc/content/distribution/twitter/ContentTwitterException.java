package com.lsc.content.distribution.twitter;

public class ContentTwitterException extends RuntimeException {

    public ContentTwitterException() {
    }

    public ContentTwitterException(String message) {
        super(message);
    }

    public ContentTwitterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentTwitterException(Throwable cause) {
        super(cause);
    }
}
