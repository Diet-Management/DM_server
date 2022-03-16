package com.management.diet.exception.Handler;

import com.management.diet.exception.ErrorCode;
import com.management.diet.exception.ErrorResponse;
import com.management.diet.exception.exception.MemberNotExistsException;
import com.management.diet.exception.exception.MemberNotFindException;
import com.management.diet.exception.exception.PasswordNotCorrectException;
import com.management.diet.exception.exception.ProfileNotExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> HttpMessageNotReadableExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex){
        printExceptionMessage(request,ex, "Not Coincide API spec");
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_COINCIDE_API);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(org.apache.http.HttpStatus.SC_BAD_REQUEST));
    }

    @ExceptionHandler(ProfileNotExistsException.class)
    public ResponseEntity<ErrorResponse> ProfileNotExistsExceptionHandler(HttpServletRequest request, HttpServletResponse response, ProfileNotExistsException ex){
        printExceptionMessage(request, ex,"Profile picture doesn't exists");
        ErrorResponse errorResponse=new ErrorResponse(ErrorCode.PROFILE_NOT_EXISTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    private void printExceptionMessage(HttpServletRequest request, Exception ex, String message) {
        log.error(request.getRequestURI());
        log.error(message);
        ex.printStackTrace();
    }
}
