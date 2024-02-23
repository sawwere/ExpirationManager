package com.mycompany.ExpirationManagerApi.controllers;

import com.mycompany.ExpirationManagerApi.controllers.helper.ControllerHelper;
import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.factories.CardDtoFactory;
import com.mycompany.ExpirationManagerApi.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CardController {

    private final CardService cardService;

    private final CardDtoFactory cardDtoFactory;

    private final ControllerHelper controllerHelper;

    public static final String CREATE_CARD = "/api/clients/{client_id}/cards";
    public static final String FIND_CARDS_BY_CLIENT = "/api/clients/{client_id}/cards";
    public static final String FIND_CARD = "/api/cards/{card_id}";
    public static final String UPDATE_CARD = "/api/cards/{card_id}";
    public static final String DELETE_CARD = "/api/cards/{card_id}";
    public static final String UPDATE_CARD_STATUS = "/api/cards/{card_id}/status";
    public static final String FIND_ALL_CARDS = "/api/cards";

    @PostMapping(CREATE_CARD)
    public CardDto createCard(
            @PathVariable(value = "client_id") Long clientId,
            @RequestBody CardDto cardDto) {
        return cardDtoFactory.make(cardService.createCard(clientId, cardDto));
    }

    @GetMapping(FIND_CARDS_BY_CLIENT)
    public List<CardDto> getCards(@PathVariable(value = "client_id") Long clientId) {
        return cardService.findAllByClient(clientId).map(cardDtoFactory::make).collect(Collectors.toList());
    }

    @GetMapping(FIND_CARD)
    public CardDto findCard(@PathVariable(value = "card_id") Long cardId) {
        return cardDtoFactory.make(cardService.findCardOrElseThrowException(cardId));
    }

    @DeleteMapping(DELETE_CARD)
    public String deleteCard(@PathVariable(value = "card_id") Long cardId) {
        cardService.deleteCard(cardId);
        return "";
    }

    @PutMapping(UPDATE_CARD_STATUS)
    public CardDto updateCardStatus(
            @PathVariable(value = "card_id") Long cardId,
            @RequestBody CardStatusDto cardStatusDto) {
        return cardDtoFactory.make(cardService.updateCardStatus(
                cardId,
                cardStatusDto)
        );
    }

    @GetMapping(FIND_ALL_CARDS)
    public List<CardDto> findAllCards() {
        return cardService.findAll().map(cardDtoFactory::make).collect(Collectors.toList());
    }
}
