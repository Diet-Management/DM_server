package com.management.diet.exception.exception;

import com.management.diet.exception.ErrorCode;

public class FileNotExistsException extends RuntimeException{
    private ErrorCode errorCode;

    public FileNotExistsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
