package com.mycompany.ExpirationManagerApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;

    @NotBlank
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("patronymic_name")
    private String patronymicName = "";

    @NotNull
    @Pattern(regexp = "\\d{10}")
    private String passport;

    @NotNull
    @Email
    private String email;

    @NotNull
    private LocalDate birthday;
}
