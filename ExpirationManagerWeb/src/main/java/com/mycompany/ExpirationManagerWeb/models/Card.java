package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Long id;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;

    private Client client;

    private CardStatus status;
}
