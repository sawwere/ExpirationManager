package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Класс для работы с сущностями типа КЛИЕНТ.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    /**
     * Идентификатор клиента
     */
    private Long id;

    /**
     * Имя клиента. Обязательное поле.
     * Непустая строка из неболее чем 64 латинских или кириллических символов
     */
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]{1,64}$", message = "Имя должно состоять из латинских или кириллических символов")
    @JsonProperty("first_name")
    private String firstName;

    /**
     * Фамилия клиента. Обязательное поле.
     * Непустая строка из неболее чем 64 латинских или кириллических символов
     */
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]{1,64}$", message = "Фамилия должна состоять из латинских или кириллических символов")
    @JsonProperty("last_name")
    private String lastName;

    /**
     * Отчество клиента. Возможно пустая строка из неболее чем 64 латинских или кириллических символов
     */
    @NotNull
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]{0,64}$", message = "Отчество должно быть пустым или состоять из латинских или кириллических символов")
    @JsonProperty("patronymic_name")
    private String patronymicName = "";

    /**
     * Паспортные данные клиента. Обязательное поле.
     * Строка должна состоять из 10 цифр без пробелов.
     */
    @NotNull
    @Pattern(regexp = "\\d{10}", message = "Паспортные данные должны состоять из 10 цифр без пробелов")
    private String passport;

    /**
     * Адрес электронной почты клиента. Обязательное поле.
     */
    @NotNull
    @Email
    private String email;

    /**
     * Дата рождения клиента. Обязательное поле.
     */
    @NotNull
    private LocalDate birthday;
}
