package com.mycompany.ExpirationManagerWeb.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для передачи данных об ошибках.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    /**
     * Строковое представление ошибки
     */
    private String error;
    /**
     * Описание ошибки
     */
    private String description;

    /**
     * Список ошибок валидации.
     * Используется для передачи информации об ошибках соответствующего типа.
     * В остальных случаях игнорируется и не передается клиенту
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("constraint_violations")
    private List<ConstraintViolation> constraintViolations;
}
