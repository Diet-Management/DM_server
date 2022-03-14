package com.management.diet.exception.exception;

import com.management.diet.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
    private ErrorCode errorCode;

    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
