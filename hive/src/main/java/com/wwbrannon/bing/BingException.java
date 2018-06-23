package com.wwbrannon.bing.exception;

public class BingException extends Exception {
    public BingException() {
        super();
    }

    public BingException(String message) {
        super(message);
    }

    public BingException(Throwable cause) {
        super(cause);
    }

    public BingException(String message, Throwable cause) {
        super(message, cause);
    }
}

