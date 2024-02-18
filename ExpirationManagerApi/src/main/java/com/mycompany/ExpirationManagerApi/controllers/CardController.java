package com.mycompany.ExpirationManagerApi.controllers;

import com.mycompany.ExpirationManagerApi.controllers.helper.ControllerHelper;
import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.exceptions.CustomException;
import com.mycompany.ExpirationManagerApi.exceptions.InvalidCardStatusException;
import com.mycompany.ExpirationManagerApi.factories.CardDtoFactory;
import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@RestController
public class CardController {

    private final CardRepository cardRepository;

    private final CardDtoFactory cardDtoFactory;

    private final ControllerHelper controllerHelper;

    public static final String CREATE_CARD = "/api/clients/{client_id}/cards";
    public static final String GET_CARDS = "/api/clients/{client_id}/cards";
    public static final String FIND_CARD = "/api/cards/{card_id}";
    public static final String UPDATE_CARD = "/api/cards/{card_id}";
    public static final String ANNUL_CARD = "/api/cards/{card_id}/annul";
    public static final String EXPIRE_CARD = "/api/cards/{card_id}/expire";
    public static final String DELETE_CARD = "/api/cards/{card_id}";
    public static final String FIND_ALL_CARDS = "/api/cards";

    @PostMapping(CREATE_CARD)
    public CardDto createCard(
            @PathVariable(value = "client_id") Long clientId,
            @RequestParam(value = "card_number") String card_number,
            @RequestParam(value = "date_of_issue") LocalDate dateOfIssie,
            @RequestParam(value = "date_of_expiration") LocalDate dateOfExpiration) {
        Client client = controllerHelper.findClientOrElseThrowException(clientId);
        Optional<Card> optionalCard = cardRepository.findByCardNumber(card_number);
        if (optionalCard.isPresent()) {
            throw new CustomException(String.format("Card with number '%s' already exists with id '%d'",
                    card_number, optionalCard.get().getId()));
        }
        Card card = Card.builder()
                .cardNumber(card_number)
                .client(client)
                .dateOfIssue(dateOfIssie)
                .dateOfExpiration(dateOfExpiration)
                .status(Card.CardStatus.OK)
                .build();
        cardRepository.save(card);
        return cardDtoFactory.make(card);
    }

    @GetMapping(GET_CARDS)
    public List<CardDto> getCards(@PathVariable(value = "client_id") Long clientId) {
        Client client = controllerHelper.findClientOrElseThrowException(clientId);
        return client.getCardList().stream().map(cardDtoFactory::make).collect(Collectors.toList());
    }

    @GetMapping(FIND_CARD)
    public CardDto findCard(@PathVariable(value = "card_id") Long card_id) {
        return cardDtoFactory.make(controllerHelper.findCardOrElseThrowException(card_id));
    }

    @DeleteMapping(DELETE_CARD)
    public String deleteCard(@PathVariable(value = "card_id") Long card_id) {
        controllerHelper.findCardOrElseThrowException(card_id);
        cardRepository.deleteById(card_id);
        return "TODO OK";
    }

    @PostMapping(ANNUL_CARD)
    public CardDto annulCard(@PathVariable(value = "card_id") Long cardId) {
        Card card = controllerHelper.findCardOrElseThrowException(cardId);
        if (card.getStatus().equals(Card.CardStatus.OK))
            card.setStatus(Card.CardStatus.ANNULLED);
        else
            throw new InvalidCardStatusException(card.getCardNumber(), Card.CardStatus.ANNULLED, card.getStatus());
        return cardDtoFactory.make(card);
    }

    @PostMapping(EXPIRE_CARD)
    public CardDto expireCard(@PathVariable(value = "card_id") Long cardId) {
        Card card = controllerHelper.findCardOrElseThrowException(cardId);
        if (card.getStatus().equals(Card.CardStatus.OK))
            card.setStatus(Card.CardStatus.EXPIRED);
        else
            throw new InvalidCardStatusException(card.getCardNumber(), Card.CardStatus.EXPIRED, card.getStatus());
        return cardDtoFactory.make(card);
    }

    @GetMapping(FIND_ALL_CARDS)
    public List<CardDto> findAllCards() {
        return cardRepository.streamAllBy().map(cardDtoFactory::make).collect(Collectors.toList());
    }
}
