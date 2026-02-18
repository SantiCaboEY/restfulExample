package com.santicabo.restful.exception;

import org.springframework.http.HttpStatus;

public class UserExistsException extends ApiException{
    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
