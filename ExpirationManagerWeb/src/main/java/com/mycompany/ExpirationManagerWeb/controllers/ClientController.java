package com.mycompany.ExpirationManagerWeb.controllers;

import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.models.Client;
import com.mycompany.ExpirationManagerWeb.service.RestClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ClientController {
    private final RestClientService restClientService;
    public static final String GET_CLIENTS = "/clients";
    public static final String CREATE_CLIENT = "/clients";
    public static final String SHOW_CLIENT = "/clients/{client_id}";
    @GetMapping(GET_CLIENTS)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("clients");
        List<Client> clients = restClientService.getClients();
        mav.addObject("clients", clients);
        Client newClient = new Client();
        mav.addObject("newClient", newClient);
        return mav;
    }

    @PostMapping(CREATE_CLIENT)
    public String create(@Valid @ModelAttribute(value="newClient") Client client) {
        restClientService.createClient(client);
        return "redirect:/clients";
    }

    @GetMapping(SHOW_CLIENT)
    public ModelAndView show(
            @PathVariable(value = "client_id") Long clientId) {
        ModelAndView mav = new ModelAndView("client_page");
        Client client = restClientService.getClient(clientId);
        mav.addObject("client", client);

        Card newCard = new Card();
        mav.addObject("newCard", newCard);

        List<Card> cardsResult = restClientService.getCardsByClientId(clientId);
        mav.addObject("cards", cardsResult);
        return mav;
    }
}
