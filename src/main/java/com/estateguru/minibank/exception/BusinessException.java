package com.estateguru.minibank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    // impl
    private HttpStatus httpStatus;
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message,HttpStatus status) {
        super(message);
    }
}
