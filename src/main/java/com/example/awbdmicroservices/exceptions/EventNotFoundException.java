package com.example.awbdmicroservices.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
