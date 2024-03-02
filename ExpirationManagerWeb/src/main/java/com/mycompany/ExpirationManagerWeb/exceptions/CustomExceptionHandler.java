package com.mycompany.ExpirationManagerWeb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.nio.channels.ClosedChannelException;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ModelAndView handleAllExceptions(Exception ex, WebRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("error");
        ErrorInfo errorInfo = ErrorInfo.builder()
                .error("Не удалось получить данные от сервера")
                .description(ex.getMessage())
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(value = {ClosedChannelException.class, ApiNotRespondingException.class})
    protected ModelAndView handleApiServerSideException(ClosedChannelException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        ErrorInfo errorInfo = ErrorInfo.builder()
                .error("Не удалось получить данные от сервера")
                .description(ex.getMessage() == null ? "Нет подключения к серверу" : ex.getMessage())
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ModelAndView handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        List<ConstraintViolation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(violation ->
                        new ConstraintViolation(
                                violation.getField(),
                                violation.getDefaultMessage()
                        )
                )
                .toList();

        ErrorInfo errorInfo = ErrorInfo.builder()
                .error("Validation error")
                .description("Введены некорректные данные")
                .constraintViolations(violations)
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }
}
