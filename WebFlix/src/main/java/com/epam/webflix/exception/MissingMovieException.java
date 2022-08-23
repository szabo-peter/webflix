package com.epam.webflix.exception;

public class MissingMovieException extends RuntimeException {

    public <T> MissingMovieException(Class<T> resource, String data) {
        super(String.format("Resource (%s) not found with the given data: %s", resource.getSimpleName(), data));
    }

}