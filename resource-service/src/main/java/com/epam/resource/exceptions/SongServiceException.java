package com.epam.resource.exceptions;

public class SongServiceException extends RuntimeException {
    public SongServiceException(String message) {
        super(message);
    }
}
