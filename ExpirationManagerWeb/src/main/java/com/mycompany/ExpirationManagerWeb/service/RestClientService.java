package com.mycompany.ExpirationManagerWeb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.dto.CardStatusDto;
import com.mycompany.ExpirationManagerWeb.exceptions.ApiClientSideException;
import com.mycompany.ExpirationManagerWeb.exceptions.ApiNotRespondingException;
import com.mycompany.ExpirationManagerWeb.exceptions.ErrorInfo;
import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.models.Client;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RestClientService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    public RestClientService() {
        objectMapper = new ObjectMapper();
        restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON))
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    ErrorInfo errorInfo =  objectMapper.readValue(response.getBody().readAllBytes(), ErrorInfo.class);
                    throw new ApiClientSideException(statusCode, errorInfo);
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError,  (request, response) -> {
                    throw new ApiNotRespondingException("Server is not responding");
                })
                .baseUrl("http://localhost:8080/api/")
                .build();
    }

    public List<Client> getClients() {
        ResponseEntity<Client[]> clients = restClient.get()
                .uri("/clients")
                .retrieve()
                .toEntity(Client[].class);
        return List.of(clients.getBody());
    }

    public void createClient(Client client) {
        String result = restClient.post()
                .uri("/clients")
                .body(client)
                .retrieve()
                .body(String.class);
    }

    public Client getClient(Long clientId) {
        return restClient.get()
                .uri("/clients/%d".formatted(clientId))
                .retrieve()
                .toEntity(Client.class).getBody();
    }

    public List<Card> getCards() {
        ResponseEntity<Card[]> cards = restClient.get()
                .uri("/cards")
                .retrieve()
                .toEntity(Card[].class);
        return List.of(cards.getBody());
    }

    public List<Card> getCardsByClientId(Long clientId) {
        ResponseEntity<Card[]> cards = restClient.get()
                .uri("/clients/%d/cards".formatted(clientId))
                .retrieve()
                .toEntity(Card[].class);
        return List.of(cards.getBody());
    }

    public void createCard(Long clientId, Card card) {
        String result = restClient.post()
                .uri("/clients/%d/cards".formatted(clientId))
                .body(card)
                .retrieve()
                .body(String.class);
    }

    public void annulCard(Long cardId) {
        String result = restClient.post()
                .uri("/cards/%d/status".formatted(cardId))
                .body(CardStatusDto.annulled())
                .retrieve()
                .body(String.class);
    }
}
