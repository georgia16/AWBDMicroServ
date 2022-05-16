package com.example.awbdmicroservices.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
