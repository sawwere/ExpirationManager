package com.mycompany.ExpirationManagerWeb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

/**
 * Исключение, генерируемое в случае возврата API сервисом ошибки с кодом 4xx в ответ на выполненный запрос
 */
@Getter
public class ApiClientSideException extends RuntimeException{
    /**
     * Код полученной ошибки
     */
    protected HttpStatusCode httpStatusCode;
    /**
     * Объект, содержащий данные об ошибке, переданные API сервисом
     */
    protected ErrorInfo errorInfo;
    public ApiClientSideException(HttpStatusCode statusCode, ErrorInfo errorInfo) {
        super(statusCode.toString() + errorInfo);
        this.httpStatusCode = statusCode;
        this.errorInfo = errorInfo;
    }
}
