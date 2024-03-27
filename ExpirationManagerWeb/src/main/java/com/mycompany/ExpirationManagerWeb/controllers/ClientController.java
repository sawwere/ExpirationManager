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

/**
 * Контроллер, отвечающий за обработку всех входящих запросов на страницах клиентов
 */
@Controller
@RequiredArgsConstructor
public class ClientController {
    private final RestClientService restClientService;
    public static final String GET_CLIENTS = "/clients";
    public static final String CREATE_CLIENT = "/clients";
    public static final String SHOW_CLIENT = "/clients/{client_id}";

    /**
     * Обрабатывает входящеий запрос на получение страницы со списком клиентов
     * @return страница со списком клиентов
     */
    @GetMapping(GET_CLIENTS)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("clients");
        List<Client> clients = restClientService.getClients();
        mav.addObject("clients", clients);
        Client newClient = new Client();
        mav.addObject("newClient", newClient);
        return mav;
    }

    /**
     * Обрабатывает входящеий запрос на создание нового клиента
     * @param client ClientDto, содержащий необходимые для создания клиента данные
     * @return переход на страницу со списком клиентов
     */
    @PostMapping(CREATE_CLIENT)
    public String create(@Valid @ModelAttribute(value="newClient") Client client) {
        restClientService.createClient(client);
        return "redirect:/clients";
    }

    /**
     * Обрабатывает входящеий запрос на получение страницы выбранного клиента
     * @param clientId идентификатор клиента, страницу которого нужно открыть
     * @return страница данного клиента
     */
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
