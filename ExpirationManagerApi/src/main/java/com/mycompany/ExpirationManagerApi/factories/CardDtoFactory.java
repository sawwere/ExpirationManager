package com.mycompany.ExpirationManagerApi.factories;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import org.springframework.stereotype.Component;

@Component
public class CardDtoFactory {
    public CardDto make(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .dateOfIssue(card.getDateOfIssue())
                .dateOfExpiration(card.getDateOfExpiration())
                .status(card.getStatus().toString())
                .build();
    }
}