package com.mycompany.ExpirationManagerWeb.exceptions;

public class ApiNotRespondingException extends RuntimeException {
    public ApiNotRespondingException(String message) {
        super(message);
    }
}
