package com.mycompany.ExpirationManagerWeb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.models.Client;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(GET_CARDS)
    public ModelAndView index() throws Exception{
        ModelAndView mav =new ModelAndView("cards");
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
                .uri("http://localhost:8080/api/clients/%s/cards".formatted(clientId))
                .body(card)
                .retrieve()
                .body(String.class);
        return "redirect:%s".formatted(ClientController.SHOW_CLIENT);
    }
}
