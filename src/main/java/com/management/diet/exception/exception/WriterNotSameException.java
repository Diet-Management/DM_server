package com.management.diet.exception.exception;

import com.management.diet.exception.ErrorCode;
import lombok.Getter;

@Getter
public class WriterNotSameException extends RuntimeException{
    private ErrorCode errorCode;

    public WriterNotSameException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
