package com.mycompany.ExpirationManagerApi.factories;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import org.springframework.stereotype.Component;

/**
 * Класс для создания объектов CardDto на основе на основе экземпляров класса  Card
 */
@Component
public class CardDtoFactory {
    public CardDto make(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .dateOfIssue(card.getDateOfIssue())
                .dateOfExpiration(card.getDateOfExpiration())
                .status(card.getStatus())
                .clientId(card.getClient().getId())
                .build();
    }
}
