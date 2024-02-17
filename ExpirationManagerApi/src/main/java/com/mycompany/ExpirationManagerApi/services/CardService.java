package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Optional<Card> findCard(Long id) {
        return cardRepository.findById(id);
    }

    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
