package com.cybertournaments.kaylemains.controller.advice;

import com.cybertournaments.kaylemains.exception.MatchNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MatchNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(MatchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String matchNotFoundHandler(MatchNotFoundException e) {
        return e.getMessage();
    }
}
