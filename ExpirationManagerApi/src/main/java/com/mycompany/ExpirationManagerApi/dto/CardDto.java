package com.mycompany.ExpirationManagerApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Класс для передачи данных о банковских картах между клиентом и сервером.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    /**
     * Идентификатор карты
     */
    private Long id;

    /**
     * Номер карты. Обязательное поле.
     * Строка, состоящая из не более чем 16 десятиных цифр
     */
    @NotNull
    @Pattern(regexp = "\\d{16}|^$", message = "Номер карты должен состоять из 0 или 16 цифр")
    @JsonProperty("card_number")
    private String cardNumber;

    /**
     * Дата выпуска карты. Обязательное поле.
     * Должна быть не позже даты истечения срока.
     */
    @NotNull
    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    /**
     * Дата истечения срока карты. Обязательное поле.
     * Должна быть не раньше даты выпуска.
     */
    @NotNull
    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;

    /**
     * Статус карты. Значение по умолчанию - OK
     */
    private CardStatus status = CardStatus.OK;

    /**
     * Идентификатор владельца карты
     */
    @JsonProperty("client_id")
    private Long clientId;
}
