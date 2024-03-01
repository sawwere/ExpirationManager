package com.mycompany.ExpirationManagerWeb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.nio.channels.ClosedChannelException;

@ControllerAdvice
public class CustomErrorHandler {
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(value = {ClosedChannelException.class, ApiNotRespondingException.class})
    public ModelAndView handleApiServerSideException(ClosedChannelException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        ErrorInfo errorInfo = ErrorInfo.builder()
                .error("Не удалось получить данные от сервера")
                .description(ex.getMessage() == null ? "Нет подключения к серверу" : ex.getMessage())
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }
}
