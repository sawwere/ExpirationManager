package com.mycompany.ExpirationManagerApi.controllers;

import com.mycompany.ExpirationManagerApi.dto.CardDto;
import com.mycompany.ExpirationManagerApi.dto.CardStatusDto;
import com.mycompany.ExpirationManagerApi.factories.CardDtoFactory;
import com.mycompany.ExpirationManagerApi.services.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер, отвечающий за обработку всех входящих запросов на действие с банковскими картами
 */
@RequiredArgsConstructor
@RestController
public class CardController {

    private final CardService cardService;

    private final CardDtoFactory cardDtoFactory;

    public static final String CREATE_CARD = "/api/clients/{client_id}/cards";
    public static final String FIND_CARDS_BY_CLIENT = "/api/clients/{client_id}/cards";
    public static final String FIND_CARD = "/api/cards/{card_id}";
    public static final String DELETE_CARD = "/api/cards/{card_id}";
    public static final String UPDATE_CARD_STATUS = "/api/cards/{card_id}/status";
    public static final String FIND_ALL_CARDS = "/api/cards";

    /**
     * Обрабатывает входящеий запрос на создание новой карты.
     * @param clientId идентификатор клиента которому создается карта
     * @param cardDto CardDto, содержащий необходимые для создания карты данные
     * @return объект CardDto содержащий данные созданной карты
     */
    @PostMapping(CREATE_CARD)
    public CardDto createCard(
            @PathVariable(value = "client_id") Long clientId,
            @RequestBody CardDto cardDto) {
        return cardDtoFactory.make(cardService.createCard(clientId, cardDto));
    }

    /**
     * Обрабатывает входящеий запроса на получение списка карт заданного клиента.
     * @param clientId идентификатор клиента, список карт которого нужно получить
     * @return список карт данного клиента
     */
    @GetMapping(FIND_CARDS_BY_CLIENT)
    public List<CardDto> getCards(@PathVariable(value = "client_id") Long clientId) {
        return cardService.findAllByClient(clientId).stream().map(cardDtoFactory::make).collect(Collectors.toList());
    }

    /**
     * Обрабатывает входящеий запрос на получение карты по ее идентификатору.
     * @param cardId идентификатор искомой карты
     * @return карта с заданным идентификатором
     */
    @GetMapping(FIND_CARD)
    public CardDto findCard(@PathVariable(value = "card_id") Long cardId) {
        return cardDtoFactory.make(cardService.findCardOrElseThrowException(cardId));
    }

    /**
     * Обрабатывает входящеий запрос на удаление карты.
     * @param cardId идентификатор карты, которую нужно удалить
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(DELETE_CARD)
    public void deleteCard(@PathVariable(value = "card_id") Long cardId) {
        cardService.deleteCard(cardId);
    }

    /**
     * Обрабатывает входящеий запрос на обновление статуса карты.
     * @param cardId  идентификатор карты, статус которой нужно изменить
     * @param cardStatusDto CardStatusDto, содержащий информацию о новом статусе
     * @return объект CardDto содержащий данные карты с измененным статусом
     */
    @PostMapping(UPDATE_CARD_STATUS)
    public CardDto updateCardStatus(
            @PathVariable(value = "card_id") Long cardId,
            @Valid @RequestBody CardStatusDto cardStatusDto) {
        return cardDtoFactory.make(cardService.updateCardStatus(
                cardId,
                cardStatusDto)
        );
    }

    /**
     *  Обрабатывает входящеий запрос на получение списка всех существующих в базе данных карт.
     * @return список всех существующих в базе данных карт
     */
    @GetMapping(FIND_ALL_CARDS)
    public List<CardDto> findAllCards() {
        return cardService.findAll().stream().map(cardDtoFactory::make).collect(Collectors.toList());
    }
}
