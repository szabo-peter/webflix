package com.epam.webflix.exception;

public class MissingUserException extends RuntimeException {

    public <T> MissingUserException(Class<T> resource, String data) {
        super(String.format("Resource (%s) not found with the given data: %s", resource.getSimpleName(), data));
    }
}
