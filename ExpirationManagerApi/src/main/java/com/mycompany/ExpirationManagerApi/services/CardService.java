package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.exceptions.CustomException;
import com.mycompany.ExpirationManagerApi.exceptions.InvalidCardStatusException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import com.mycompany.ExpirationManagerApi.storage.entities.*;
import com.mycompany.ExpirationManagerApi.storage.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final ClientService clientService;
    public Optional<Card> findCard(Long id) {
        return cardRepository.findById(id);
    }

    public Card findCardOrElseThrowException(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(String.format("Card with id '%s' doesn't exist", cardId))
                );
    }

    public Boolean isCardNumberValid(String cardNumber) {
        return cardNumber.matches("/d[16]");
    }
    public String generateCardNumber() {
        String str = Instant.now().toString();
        System.out.println(str);
        return str;
    }

    public Card createCard(Long clientId, CardDto cardDto) {
        var optionalClient = clientService.findClient(clientId);
        if (optionalClient.isEmpty()) {
            throw new NotFoundException(
                    String.format("Client with id '%s' doesn't exist", clientId)
            );
        }
        if (cardRepository.findByCardNumber(cardDto.getCardNumber()).isPresent()) {
            throw new CustomException(
                    String.format("Card with number '%s' already exists", cardDto.getCardNumber())
            );
        }
        Card card = Card.builder()
                .cardNumber(cardDto.getCardNumber())
                .dateOfIssue(cardDto.getDateOfIssue())
                .dateOfExpiration(cardDto.getDateOfExpiration())
                .status(CardStatus.OK)
                .client(optionalClient.get())
                .build();
        cardRepository.save(card);
        return card;
    }

    public Card updateCardStatus(Long cardId, CardStatusDto cardStatusDto) {
        if (!cardStatusDto.isValid())
            throw new InvalidCardStatusException(
                    String.format("Invalid card status '%s' was given", cardStatusDto.getStatus())
            );
        Card card = findCardOrElseThrowException(cardId);
        if (card.getStatus() != CardStatus.OK)
            throw new InvalidCardStatusException(
                    String.format("Card '%s' cannot be '%s' due to it's status '%s'",
                        card.getCardNumber(),
                        cardStatusDto.getStatus(),
                        card.getStatus().toString())
            );
        card.setStatus(cardStatusDto.getCardStatus());
        cardRepository.save(card);
        return card;
    }

    public void deleteCard(Long cardId) {
        findCardOrElseThrowException(cardId);
        cardRepository.deleteById(cardId);
    }

    public Stream<Card> findAllByClient(Long clientId) {
        Client client = clientService.findClientOrElseThrowException(clientId);
        return client.getCardList().stream();
    }

    public Stream<Card> findAll() {
        return cardRepository.streamAllBy();
    }
}
