package com.mycompany.ExpirationManagerWeb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Long id;

    @NotNull
    @JsonProperty("card_number")
    private String cardNumber;

    @NotNull
    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    @NotNull
    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;


    private CardStatus status;
}
