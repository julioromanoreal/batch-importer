package com.julioromano.batchimporter.exceptions;

public class ProcessingException extends RuntimeException {

    public ProcessingException(Throwable cause) {
        super(cause);
    }

    public ProcessingException(String message) {
        super(message);
    }

}
