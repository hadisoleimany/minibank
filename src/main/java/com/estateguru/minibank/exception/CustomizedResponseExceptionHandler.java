package com.estateguru.minibank.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.UUID;

@ControllerAdvice
@RestController
public class CustomizedResponseExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handlerExceptions(BusinessException ex, WebRequest request) {

        String message = ex.getMessage();


        ExceptionResponse response = new ExceptionResponse(message, request.getDescription(false), new Date(),
                ex.getHttpStatus() == null ? HttpStatus.BAD_REQUEST : ex.getHttpStatus()
                , UUID.randomUUID());
        return new ResponseEntity<>(response, response.getHttpStatus());

    }
}
