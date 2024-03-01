package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
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
    @Pattern(regexp = "\\d[10]")
    private String passport;

    @NotNull
    @Email
    private String email;

    @NotNull
    private LocalDate birthday;
}
