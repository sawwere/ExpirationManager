package com.mycompany.ExpirationManagerWeb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Обработчик возникающих в процессе работы с API 4xx ошибок.
 */
@ControllerAdvice
public class ApiErrorHandler{
    /**
     * Перехватывает вознкиющие исключения типа ApiClientSideException и рапределяет по обработчикам.
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
    @ExceptionHandler(value = {ApiClientSideException.class})
    public ModelAndView handleApiClientSideException(ApiClientSideException ex, WebRequest request) {
        switch (ex.getHttpStatusCode().value())
        {
            case (400) : return handleApiBadRequest(ex, request);
            case (404) : return handleApiNotFound(ex, request);
            default: return handleApiDefaultError(ex, request);
        }
    }

    /**
     * Обрабатывает ошибки с кодом 400
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ModelAndView handleApiBadRequest(ApiClientSideException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(HttpStatus.BAD_REQUEST);
        ex.getErrorInfo().setError("В запросе содержалась ошибка");
        mav.addObject("errorInfo", ex.getErrorInfo());
        return mav;
    }

    /**
     * Обрабатывает ошибки с кодом 404
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ModelAndView handleApiNotFound(ApiClientSideException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(HttpStatus.NOT_FOUND);
        ex.getErrorInfo().setError("Искомый ресурс не найден");
        mav.addObject("errorInfo", ex.getErrorInfo());
        return mav;
    }

    /**
     * Обрабатывает все необработанные другими методами ошибки
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ModelAndView handleApiDefaultError(ApiClientSideException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(HttpStatus.BAD_REQUEST);
        mav.addObject("errorInfo", ex.getErrorInfo());
        return mav;
    }
}
