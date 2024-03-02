package com.mycompany.ExpirationManagerApi.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    private String error;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ConstraintViolation> constraintViolations;
}
