package com.santicabo.restful.exception;

import com.santicabo.restful.dto.ErrorDto;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    public ApiException(String message, Throwable cause){
        super(message,cause);
    }

    public ApiException(String message){
        super(message);
    }
    public ErrorDto getError(){
        return new ErrorDto(getMessage());
    }

    public HttpStatus getStatus(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
