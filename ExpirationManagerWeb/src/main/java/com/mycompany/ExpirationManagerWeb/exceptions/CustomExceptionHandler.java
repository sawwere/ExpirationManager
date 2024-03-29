package com.mycompany.ExpirationManagerWeb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Глобальный обработчик возникающих в процессе работы приложения исключений.
 * Служит для возвращение сообщений об ошибках в едином формате при их возникновении.
 */
@ControllerAdvice
public class CustomExceptionHandler {
    /**
     * Базовый обработчик для ловли непойманных другими методами исключений
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ModelAndView handleAllExceptions(Exception ex, WebRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("error");
        ErrorInfo errorInfo = ErrorInfo.builder()
                .error("Не удалось обработать запрос")
                .description(ex.getMessage())
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }

    /**
     * Обрабатывает исключения, генерируемые в случае возврата API сервисом ошибки с кодом 5xx
     * или остутсвия доступа к нему.
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(value = {ResourceAccessException.class, ApiNotRespondingException.class })
    protected ModelAndView handleApiServerSideException(RuntimeException ex, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        ErrorInfo errorInfo = ErrorInfo.builder()
                .error("Не удалось получить данные от сервера")
                .description("Нет подключения к серверу")
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }

    /**
     * Обрабатывает исключения, связанные с передачей некорректных данных
     * @param ex возникшее исключение
     * @param request текущий запрос
     * @return страница, содержащая информацию об ошибке
     */
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
                .error("Ошибка валидации")
                .description("Введены некорректные данные")
                .constraintViolations(violations)
                .build();
        mav.addObject("errorInfo", errorInfo);
        return mav;
    }
}
