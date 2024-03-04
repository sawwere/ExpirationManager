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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{0,16}", message = "Номер карты должен состоять из 0-16 цифр")
    @JsonProperty("card_number")
    private String cardNumber;

    @NotNull
    @JsonProperty("date_of_issue")
    private LocalDate dateOfIssue;

    @NotNull
    @JsonProperty("date_of_expiration")
    private LocalDate dateOfExpiration;

    private CardStatus status = CardStatus.OK;
}
