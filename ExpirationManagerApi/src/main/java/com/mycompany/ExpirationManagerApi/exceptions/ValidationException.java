package com.mycompany.ExpirationManagerApi.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException{
    protected List<ConstraintViolation> constraintViolations;
    public ValidationException(String message, Collection<ConstraintViolation> constraintViolations) {
        super(message);
        this.constraintViolations = new ArrayList<>(constraintViolations);

    }
}
