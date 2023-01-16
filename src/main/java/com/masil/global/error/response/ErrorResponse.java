package com.masil.global.error.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;
@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private List<FieldError> errors;

}

