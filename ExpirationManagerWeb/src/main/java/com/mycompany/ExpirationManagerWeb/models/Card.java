package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{16}|^$", message = "Номер карты должен состоять из 0 или 16 цифр")
    @JsonProperty("card_number")
    private String cardNumber;

    @NotNull
    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    @NotNull
    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;

    private CardStatus status = CardStatus.OK;

    @JsonProperty("client_id")
    private Long clientId;
}

