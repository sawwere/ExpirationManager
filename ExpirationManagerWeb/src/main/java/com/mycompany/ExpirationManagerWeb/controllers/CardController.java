package com.mycompany.ExpirationManagerWeb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.models.Card;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CardController {
    private final ObjectMapper objectMapper;
    public static final String GET_CARDS = "/cards";
    public static final String CREATE_CARD = "/clients/{client_id}/cards";

    public static final String ANNUL_CARD = "/cards/{card_id}/status/annul";

    @GetMapping(GET_CARDS)
    public ModelAndView index() throws Exception{
        ModelAndView mav = new ModelAndView("cards");
        RestClient defaultClient = RestClient.create();
        String result = defaultClient.get()
                .uri("http://localhost:8080/api/cards")
                .retrieve()
                .body(String.class);
        Card[] cards =  objectMapper.readValue(result, Card[].class);
        mav.addObject("cards", cards);
        return mav;
    }

    @PostMapping(CREATE_CARD)
    public String create(
            @PathVariable (value = "client_id") Long clientId,
            Card card) throws Exception{
        RestClient defaultClient = RestClient.create();
        String result = defaultClient.post()
                .uri("http://localhost:8080/api/clients/%d/cards".formatted(clientId))
                .body(card)
                .retrieve()
                .body(String.class);
        return "redirect:%s".formatted(ClientController.SHOW_CLIENT);
    }

    @GetMapping(ANNUL_CARD)
    public String annulCard(@PathVariable(value = "card_id") Long cardId,
                            HttpServletRequest request) {
        RestClient defaultClient = RestClient.create();
        String result = defaultClient.put()
                .uri("http://localhost:8080/api/cards/%d/status".formatted(cardId))
                .contentType(MediaType.APPLICATION_JSON)
                .body("{status\" : \"ANNULLED\"}")
                .retrieve()
                .body(String.class);
        return "redirect:" + request.getHeader("Referer");
    }
}
