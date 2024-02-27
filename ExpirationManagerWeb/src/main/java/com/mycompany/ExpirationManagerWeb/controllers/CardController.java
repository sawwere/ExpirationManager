package com.mycompany.ExpirationManagerWeb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CardController {
    private final ObjectMapper objectMapper;
    public static final String GET_CARDS = "/cards";
    public static final String CREATE_CARD = "/clients/{client_id}/cards";
    public static final String GET_CARDS_BY_CLIENT = "/clients/{client_id}";

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
        Card newCard = new Card();
        mav.addObject("newClient", newCard);
        return mav;
    }

    @PostMapping(CREATE_CARD)
    public String create(Card card) throws Exception{
        RestClient defaultClient = RestClient.create();

        return "redirect:/cards";
    }
}
