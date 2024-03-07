package com.mycompany.ExpirationManagerWeb.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {
    private String error;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("constraint_violations")
    private List<ConstraintViolation> constraintViolations;
}
