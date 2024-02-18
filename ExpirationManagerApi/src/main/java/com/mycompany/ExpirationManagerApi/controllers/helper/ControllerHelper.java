package com.mycompany.ExpirationManagerApi.controllers.helper;

import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.CardRepository;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component

public class ControllerHelper {
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;

    public Client findClientOrElseThrowException(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id '%s' doesn't exist", clientId))
                );
    }

    public Card findCardOrElseThrowException(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(String.format("Card with id '%s' doesn't exist", cardId))
                );
    }
}
