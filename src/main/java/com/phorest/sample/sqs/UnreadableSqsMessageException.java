package com.phorest.sample.sqs;

public class UnreadableSqsMessageException extends RuntimeException {

    public UnreadableSqsMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
