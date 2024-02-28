package com.mycompany.ExpirationManagerWeb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class ClientController {
    private final ObjectMapper objectMapper;
    public static final String GET_CLIENTS = "/clients";
    public static final String CREATE_CLIENT = "/clients";
    public static final String SHOW_CLIENT = "/clients/{client_id}";

    @GetMapping(GET_CLIENTS)
    public ModelAndView index() throws Exception{
        ModelAndView mav = new ModelAndView("clients");
        RestClient defaultClient = RestClient.create();
        String result = defaultClient.get()
                .uri("http://localhost:8080/api/clients")
                .retrieve()
                .body(String.class);
        Client[] clients =  objectMapper.readValue(result, Client[].class);
        mav.addObject("clients", clients);
        Client newClient = new Client();
        mav.addObject("newClient", newClient);
        return mav;
    }

    @PostMapping(CREATE_CLIENT)
    public String create(Client client) throws Exception{
        RestClient defaultClient = RestClient.create();
        String result = defaultClient.post()
                .uri("http://localhost:8080/api/clients")
                .body(client)
                .retrieve()
                .body(String.class);
        return "redirect:/clients";
    }

    @GetMapping(SHOW_CLIENT)
    public ModelAndView show(@PathVariable(value = "client_id") Long clientId) throws Exception{
        ModelAndView mav =new ModelAndView("client_page");
        RestClient defaultClient = RestClient.create();
        String result = defaultClient.get()
                .uri("http://localhost:8080/api/clients/%d".formatted(clientId))
                .retrieve()
                .body(String.class);
        Client client = objectMapper.readValue(result, Client.class);
        mav.addObject("client", client);
        Card newCard = new Card();
        mav.addObject("newCard", newCard);
        String cardsResult = defaultClient.get()
                .uri("http://localhost:8080/api/clients/%d/cards".formatted(clientId))
                .retrieve()
                .body(String.class);
        Card[] cards = objectMapper.readValue(cardsResult, Card[].class);
        mav.addObject("cards", cards);
        return mav;
    }
}
