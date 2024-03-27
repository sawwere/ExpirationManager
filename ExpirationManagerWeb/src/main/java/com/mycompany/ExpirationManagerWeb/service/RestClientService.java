package com.mycompany.ExpirationManagerWeb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ExpirationManagerWeb.dto.CardStatusDto;
import com.mycompany.ExpirationManagerWeb.exceptions.ApiClientSideException;
import com.mycompany.ExpirationManagerWeb.exceptions.ApiNotRespondingException;
import com.mycompany.ExpirationManagerWeb.exceptions.ErrorInfo;
import com.mycompany.ExpirationManagerWeb.models.Card;
import com.mycompany.ExpirationManagerWeb.models.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Отвечает за общение с API сервисом, обработку полученных данных.
 */
@Service
public class RestClientService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public RestClientService(@Value("${api.baseUrl}") String baseUrl) {
        objectMapper = new ObjectMapper();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(500);
        factory.setReadTimeout(1500);
        restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON))
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    ErrorInfo errorInfo =  objectMapper.readValue(response.getBody().readAllBytes(), ErrorInfo.class);
                    throw new ApiClientSideException(statusCode, errorInfo);
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError,  (request, response) -> {
                    throw new ApiNotRespondingException("Произошла внутренняя ошибка сервера");
                })
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    /**
     * Выполняет запрос на получение списка клиентов.
     * @return список клиентов
     */
    public List<Client> getClients() {
        ResponseEntity<Client[]> clients = restClient.get()
                .uri("/clients")
                .retrieve()
                .toEntity(Client[].class);
        return List.of(clients.getBody());
    }

    /**
     * Выполняет запрос на создание клиента.
     * @param client ClientDto, содержащий необходимые для создания клиента данные
     */
    public void createClient(Client client) {
        String result = restClient.post()
                .uri("/clients")
                .body(client)
                .retrieve()
                .body(String.class);
    }

    /**
     * Выполняет запрос на получение клиента.
     * @param clientId идентификатор искомого клиента
     * @return искомый клиент
     */
    public Client getClient(Long clientId) {
        return restClient.get()
                .uri("/clients/%d".formatted(clientId))
                .retrieve()
                .toEntity(Client.class).getBody();
    }

    /**
     * Выполняет запрос на получение списка банковских карт.
     * @return список банковских карт
     */
    public List<Card> getCards() {
        ResponseEntity<Card[]> cards = restClient.get()
                .uri("/cards")
                .retrieve()
                .toEntity(Card[].class);
        return List.of(cards.getBody());
    }

    /**
     * Выполняет запрос на получение списка банковских карт заданного клиента.
     * @param clientId идентификатор клиента-владельца карты
     * @return список банковских карт заданного клиента
     */
    public List<Card> getCardsByClientId(Long clientId) {
        ResponseEntity<Card[]> cards = restClient.get()
                .uri("/clients/%d/cards".formatted(clientId))
                .retrieve()
                .toEntity(Card[].class);
        return List.of(cards.getBody());
    }

    /**
     * Выполняет запрос на создание карты.
     * @param clientId идентификатор клиента-владельца карты
     * @param card CardDto, содержащий необходимые для создания карты данные
     */
    public void createCard(Long clientId, Card card) {
        String result = restClient.post()
                .uri("/clients/%d/cards".formatted(clientId))
                .body(card)
                .retrieve()
                .body(String.class);
    }

    /**
     * Выполняет запрос на аннулирование карты.
     * @param cardId идентификатор карты, статус которой нужно изменить
     */
    public void annulCard(Long cardId) {
        String result = restClient.post()
                .uri("/cards/%d/status".formatted(cardId))
                .body(CardStatusDto.annulled())
                .retrieve()
                .body(String.class);
    }
}
