package com.mycompany.ExpirationManagerApi.exceptions;

import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCardStatusException extends RuntimeException {
    public InvalidCardStatusException(String cardNumber, Card.CardStatus expected, Card.CardStatus got) {
        super(String.format("Card '%d' cannot be expired due to it's status '%s'", cardNumber, expected, got));
    }
}
