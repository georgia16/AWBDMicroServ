package com.example.awbdmicroservices.exceptions;

public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException() {
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}
