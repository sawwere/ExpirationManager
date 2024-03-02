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


@RequiredArgsConstructor
@RestController
public class ClientController {

    private final ClientDtoFactory clientDtoFactory;

    private final ClientService clientService;

    public static final String CREATE_CLIENT = "/api/clients";
    public static final String UPDATE_CLIENT = "/api/clients/{client_id}";
    public static final String FIND_CLIENT = "/api/clients/{client_id}";
    public static final String DELETE_CLIENT = "/api/clients/{client_id}";
    public static final String FIND_ALL_CLIENTS = "/api/clients";

    @PostMapping(CREATE_CLIENT)
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto createClient(@Valid @RequestBody ClientDto clientDto) {
        return clientDtoFactory.make(clientService.createClient(clientDto));
    }

    @PatchMapping(UPDATE_CLIENT)
    public ClientDto updateClient(
            @PathVariable(value = "client_id") Long clientId,
            @RequestParam(value = "passport", required = false) Optional<String> passport,
            @RequestParam(value = "email", required = false) Optional<String> email,
            @RequestParam(value = "first_name", required = false) Optional<String> firstName,
            @RequestParam(value = "last_name", required = false) Optional<String> lastName,
            @RequestParam(value = "patronymic_name", required = false) Optional<String> patronymicName,
            @RequestParam(value = "birthday", required = false) Optional<LocalDate> birthdate) {
        return clientDtoFactory.make(
                clientService.updateClient(clientId, passport, email, firstName, lastName, patronymicName, birthdate)
        );
    }

    @GetMapping(FIND_CLIENT)
    public ClientDto findClient(@PathVariable(value = "client_id") Long clientId) {
        return clientDtoFactory.make(clientService.findClientOrElseThrowException(clientId));
    }

    @DeleteMapping(DELETE_CLIENT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable("client_id") Long clientId) {
        clientService.deleteClient(clientId);
    }

    @GetMapping(FIND_ALL_CLIENTS)
    public List<ClientDto> findAllClients() {
        return clientService.findAll().stream().map(clientDtoFactory::make).collect(Collectors.toList());
    }

}
