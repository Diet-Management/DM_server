package com.management.diet.exception.exception;

import com.management.diet.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AccessTokenExpiredException extends RuntimeException{
    private ErrorCode errorCode;

    public AccessTokenExpiredException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
