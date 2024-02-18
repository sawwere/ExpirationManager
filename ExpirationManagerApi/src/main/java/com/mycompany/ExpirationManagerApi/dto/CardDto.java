package com.mycompany.ExpirationManagerApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;

    private String status;
}
