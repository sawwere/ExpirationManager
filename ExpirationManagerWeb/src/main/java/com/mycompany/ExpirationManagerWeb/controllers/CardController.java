package com.mycompany.ExpirationManagerWeb.controllers;

import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.service.RestClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Контроллер, отвечающий за обработку всех входящих запросов на действия с банковскими картами
 */
@Controller
@RequiredArgsConstructor
public class CardController {
    private final RestClientService restClientService;
    public static final String GET_CARDS = "/cards";
    public static final String CREATE_CARD = "/clients/{client_id}/cards";
    public static final String ANNUL_CARD = "/cards/{card_id}/status/annul";

    /**
     * Обрабатывает входящеий запрос на получение страницы со списком банковских карт.
     * @return страница со списком банковских карт
     */
    @GetMapping(GET_CARDS)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("cards");
        List<Card> cards = restClientService.getCards();
        mav.addObject("cards", cards);
        return mav;
    }

    /**
     * Обрабатывает входящеий запрос на создание новой карты.
     * @param clientId идентификатор клиента-владельца карты
     * @param card CardDto, содержащий необходимые для создания карты данные
     * @return переход на страницу данного клиента
     */
    @PostMapping(CREATE_CARD)
    public String create(
            @PathVariable (value = "client_id") Long clientId,
            @Valid Card card) {
        restClientService.createCard(clientId, card);
        return "redirect:%s".formatted(ClientController.SHOW_CLIENT);
    }

    /**
     *Обрабатывает входящеий запрос на аннулирование карты.
     * @param cardId идентификатор карты, статус которой нужно изменить
     * @param httpServletRequest текущий запрос
     * @return переход на страницу, с которой был вызван метод
     */
    @GetMapping(ANNUL_CARD)
    public String annulCard(@PathVariable(value = "card_id") Long cardId,
                            HttpServletRequest httpServletRequest) {
        restClientService.annulCard(cardId);
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }
}
