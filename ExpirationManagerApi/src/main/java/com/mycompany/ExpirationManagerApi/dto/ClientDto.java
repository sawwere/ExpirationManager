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
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]{1,64}$", message = "Имя должно состоять из латинских или кириллических символов")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]{1,64}$", message = "Фамилия должна состоять из латинских или кириллических символов")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]{0,64}$", message = "Отчество должно быть пустым или состоять из латинских или кириллических символов")
    @JsonProperty("patronymic_name")
    private String patronymicName = "";

    @NotNull
    @Pattern(regexp = "\\d{10}", message = "Паспортные данные должны состоять из 10 цифр без пробелов")
    private String passport;

    @NotNull
    @Email
    private String email;

    @NotNull
    private LocalDate birthday;
}
