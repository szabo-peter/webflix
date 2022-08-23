package com.epam.webflix.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MissingMovieException.class)
    public String handleMissingIdException(MissingMovieException missingMovieException) {
        log.info("MissingIDException happened: ", missingMovieException);
        return missingMovieException.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleFavouriteMovieException(ConstraintViolationException constraintViolationException) {
        log.info("Validation error: ", constraintViolationException);
        return constraintViolationException.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public String handleEverything(Throwable throwable) {
        log.info("Something went wrong : ", throwable);
        return throwable.getMessage();
    }
}