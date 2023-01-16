package com.masil.global.error.handler;

import com.masil.global.error.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse inValidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse response = new ErrorResponse();
//
//        for (FieldError erorr : e.getFieldErrors()) {
//            response.addValidation(erorr.getField(), erorr.getDefaultMessage());
//        }
        return response;
    }
}
