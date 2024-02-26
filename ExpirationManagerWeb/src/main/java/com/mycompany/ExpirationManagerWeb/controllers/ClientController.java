package com.mycompany.ExpirationManagerWeb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ClientController {
    private final ObjectMapper objectMapper;
    @GetMapping("/clients")
    public ModelAndView getClients() throws Exception{
        ModelAndView mav =new ModelAndView("clients");
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
}
