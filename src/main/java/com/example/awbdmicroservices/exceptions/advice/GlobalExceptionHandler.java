package com.example.awbdmicroservices.exceptions.advice;

import com.example.awbdmicroservices.exceptions.EventNotFoundException;
import com.example.awbdmicroservices.exceptions.PersonNotFoundException;
import com.example.awbdmicroservices.exceptions.ReservationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ReservationNotFoundException.class, PersonNotFoundException.class, EventNotFoundException.class})
    public ResponseEntity<String> handle(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage() + "at" + LocalDateTime.now());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handle(MethodArgumentNotValidException exception) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", ")));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handle(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
