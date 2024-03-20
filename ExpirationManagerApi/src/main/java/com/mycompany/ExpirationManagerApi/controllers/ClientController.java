package com.mycompany.ExpirationManagerApi.controllers;

import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.factories.ClientDtoFactory;
import com.mycompany.ExpirationManagerApi.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Контроллер, отвечающий за обработку всех входящих запросов на действие с клиентами.
 */
@RequiredArgsConstructor
@RestController
public class ClientController {

    private final ClientDtoFactory clientDtoFactory;

    private final ClientService clientService;

    public static final String CREATE_CLIENT = "/api/clients";
    public static final String FIND_CLIENT = "/api/clients/{client_id}";
    public static final String DELETE_CLIENT = "/api/clients/{client_id}";
    public static final String FIND_ALL_CLIENTS = "/api/clients";

    /**
     * Обрабатывает входящеий запрос на создание нового клиента.
     * @param clientDto объект, содержащий необходимые для создания карты данные
     * @return ClientDto, содержащий данные созданной карты
     */
    @PostMapping(CREATE_CLIENT)
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto createClient(@Valid @RequestBody ClientDto clientDto) {
        return clientDtoFactory.make(clientService.createClient(clientDto));
    }

    /**
     * Обрабатывает входящеий запрос на получение клиента по его идентификатору.
     * @param clientId идентификатор искомого клиента
     * @return клиент с заданным идентификатором
     */
    @GetMapping(FIND_CLIENT)
    public ClientDto findClient(@PathVariable(value = "client_id") Long clientId) {
        return clientDtoFactory.make(clientService.findClientOrElseThrowException(clientId));
    }

    /**
     * Обрабатывает входящеий запрос на удаление клиента.
     * @param clientId идентификатор клиента, которого нужно удалить
     */
    @DeleteMapping(DELETE_CLIENT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable("client_id") Long clientId) {
        clientService.deleteClient(clientId);
    }

    /**
     * Обрабатывает входящеий запрос на получение списка всех существующих в базе данных клиентов.
     * @return список всех клиентов
     */
    @GetMapping(FIND_ALL_CLIENTS)
    public List<ClientDto> findAllClients() {
        return clientService.findAll().stream().map(clientDtoFactory::make).collect(Collectors.toList());
    }

}
