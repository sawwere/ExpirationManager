package com.mycompany.ExpirationManagerWeb.controllers;

import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.service.RestClientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardController {
    private final RestClientService restClientService;
    public static final String GET_CARDS = "/cards";
    public static final String CREATE_CARD = "/clients/{client_id}/cards";
    public static final String ANNUL_CARD = "/cards/{card_id}/status/annul";

    @GetMapping(GET_CARDS)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("cards");
        List<Card> cards = restClientService.getCards();
        mav.addObject("cards", cards);
        return mav;
    }

    @PostMapping(CREATE_CARD)
    public String create(
            @PathVariable (value = "client_id") Long clientId,
            Card card) {
        restClientService.createCard(clientId, card);
        return "redirect:%s".formatted(ClientController.SHOW_CLIENT);
    }

    @GetMapping(ANNUL_CARD)
    public String annulCard(@PathVariable(value = "card_id") Long cardId,
                            HttpServletRequest httpServletRequest) {
        restClientService.annulCard(cardId);
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }
}
