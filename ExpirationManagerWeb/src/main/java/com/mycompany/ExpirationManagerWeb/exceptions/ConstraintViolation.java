package com.mycompany.ExpirationManagerWeb.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для хранения информации об ошибках валидации данных.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintViolation {
    /**
     * Название поля, при валидации которого произошла ошибка
     */
    private String field;
    /**
     * Сообщение сопутствующее ошибке
     */
    private String message;

    /**
     * Возвращет строковое представление объекта.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
