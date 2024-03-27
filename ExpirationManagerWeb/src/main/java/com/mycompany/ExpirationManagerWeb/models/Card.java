package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

/**
 * Класс для работы с сущностями типа БАНКОВСКАЯ КАРТА.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    /**
     * Идентификатор карты
     */
    private Long id;

    /**
     * Номер карты.
     */
    @NotNull
    @Pattern(regexp = "\\d{16}|^$", message = "Номер карты должен состоять из 0 или 16 цифр")
    @JsonProperty("card_number")
    private String cardNumber;

    /**
     * Дата выпуска карты.
     */
    @NotNull
    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    /**
     * Дата истечения срока карты.
     */
    @NotNull
    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;

    /**
     * Статус карты.
     */
    private CardStatus status = CardStatus.OK;

    /**
     * Идентификатор владелька карты.
     */
    @JsonProperty("client_id")
    private Long clientId;
}

