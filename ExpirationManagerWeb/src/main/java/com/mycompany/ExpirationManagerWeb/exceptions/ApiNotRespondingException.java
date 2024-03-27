package com.mycompany.ExpirationManagerWeb.exceptions;

/**
 * Исключение, генерируемое в случае возврата API сервисом ошибки с кодом 5xx в ответ на выполненный запрос
 */
public class ApiNotRespondingException extends RuntimeException {
    public ApiNotRespondingException(String message) {
        super(message);
    }
}
