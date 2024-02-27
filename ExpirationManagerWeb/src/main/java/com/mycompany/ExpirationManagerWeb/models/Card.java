package com.mycompany.ExpirationManagerWeb.models;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Long id;

    private String cardNumber;

    private LocalDate dateOfIssue;

    private LocalDate dateOfExpiration;

    private Client client;

    private CardStatus status;
}
