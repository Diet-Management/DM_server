package com.management.diet.exception.Handler;

import com.management.diet.exception.ErrorResponse;
import com.management.diet.exception.exception.MemberNotExistsException;
import com.management.diet.exception.exception.MemberNotFindException;
import com.management.diet.exception.exception.PasswordNotCorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MemberNotExistsException.class)
    public ResponseEntity<ErrorResponse> MemberNotExistsExceptionHandler(HttpServletRequest request, HttpServletResponse response, MemberNotExistsException ex){
        printExceptionMessage(request, ex, "UserNoExistsException");
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(PasswordNotCorrectException.class)
    public ResponseEntity<ErrorResponse> PasswordNotCorrectExceptionHandler(HttpServletRequest request, HttpServletResponse response, PasswordNotCorrectException ex){
        printExceptionMessage(request, ex, "Password is not correct");
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MemberNotFindException.class)
    public ResponseEntity<ErrorResponse> MemberNotFindExceptionHandler(HttpServletRequest request, HttpServletResponse response, MemberNotFindException ex){
        printExceptionMessage(request,ex, "member can't find");
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    private void printExceptionMessage(HttpServletRequest request, RuntimeException ex, String message) {
        log.error(request.getRequestURI());
        log.error(message);
        ex.printStackTrace();
    }
}
