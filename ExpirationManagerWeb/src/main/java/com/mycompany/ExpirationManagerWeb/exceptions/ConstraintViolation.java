package com.mycompany.ExpirationManagerWeb.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintViolation {
    private String field;
    private String message;

    @Override
    public String toString() {
        return "{" +
                "field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
