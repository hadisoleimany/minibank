package com.estateguru.minibank.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.UUID;

@Data
public class ExceptionResponse {
    private Date timeStamp;
    private String message;
    private String details;
    private HttpStatus httpStatus;
    private UUID uuid;

    public ExceptionResponse( String message, String details,Date timeStamp, HttpStatus httpStatus, UUID uuid) {
        this(message,details,timeStamp,uuid);
        this.httpStatus = httpStatus;
    }
    public ExceptionResponse( String message, String details,Date timeStamp, UUID uuid) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
        this.uuid=uuid;
    }

}
