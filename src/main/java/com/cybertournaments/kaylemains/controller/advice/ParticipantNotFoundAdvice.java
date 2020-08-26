package com.cybertournaments.kaylemains.controller.advice;

import com.cybertournaments.kaylemains.exception.ParticipantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ParticipantNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ParticipantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String participantNotFoundHandler(ParticipantNotFoundException e) {
        return e.getMessage();
    }
}
