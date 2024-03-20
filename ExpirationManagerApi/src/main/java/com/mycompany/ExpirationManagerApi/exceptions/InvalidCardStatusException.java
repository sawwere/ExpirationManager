package com.mycompany.ExpirationManagerApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, генерируемое в случае попытки изменения статуса карты, имеющей статус, отличный от OK.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCardStatusException extends RuntimeException {
    public InvalidCardStatusException(String message) {
        super(message);
    }
}
