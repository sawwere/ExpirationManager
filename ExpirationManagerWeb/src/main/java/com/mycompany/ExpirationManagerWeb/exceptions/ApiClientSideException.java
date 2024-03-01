package com.mycompany.ExpirationManagerWeb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;


@Getter
public class ApiClientSideException extends RuntimeException{
    protected HttpStatusCode httpStatusCode;
    protected ErrorInfo errorInfo;
    public ApiClientSideException(HttpStatusCode statusCode, ErrorInfo errorInfo) {
        super(statusCode.toString() + errorInfo);
        this.httpStatusCode = statusCode;
        this.errorInfo = errorInfo;
    }
}
