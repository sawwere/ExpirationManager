package com.mycompany.ExpirationManagerApi.controllers;

import com.mycompany.ExpirationManagerApi.exceptions.CustomException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    public static final String CREATE_OR_UPDATE_CLIENT = "/api/clients";
    public static final String FIND_CLIENT = "/api/clients/{client_id}";
    public static final String DELETE_CLIENT = "/api/clients/{client_id}";
    public static final String FIND_ALL_CLIENTS = "/api/clients";
    @PostMapping(CREATE_OR_UPDATE_CLIENT)
    public Client createClient(
            @RequestParam(value = "client_id", required = false) Optional<Long> clientId,
            @RequestParam(value = "passport") String passport,
            @RequestParam(value = "first_name") String firstName,
            @RequestParam(value = "last_name") String lastName,
            @RequestParam(value = "birthday") LocalDate birthdate) {
        Client client = new Client();
        // Switch between creating and updating
        if (clientId.isEmpty()) {
            // check for unique passport
            var optionalClient = clientRepository.findByPassport(passport);
            if (optionalClient.isPresent()) {
                throw new CustomException(String.format("Client with passport '%s' already exists with id %d",
                        passport, optionalClient.get().getId()));
            }
            client = Client.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .passport(passport)
                    .birthday(birthdate)
                    .build();
        }
        else {
            client = findClientOrElseThrowException(clientId.get());
            client.setPassport(passport);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setBirthday(birthdate);
        }

        clientRepository.save(client);
        return client;
    }

    @GetMapping(FIND_CLIENT)
    public Client findClient(
            @PathVariable(value = "client_id") Long clientId) {
        return findClientOrElseThrowException(clientId);
    }

    @DeleteMapping(DELETE_CLIENT)
    public String deleteProject(@PathVariable("client_id") Long clientId) {

        findClientOrElseThrowException(clientId);
        clientRepository.deleteById(clientId);
        return "TODO OK";
    }

    @GetMapping(FIND_ALL_CLIENTS)
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    private Client findClientOrElseThrowException(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id '%s' doesn't exist", clientId))
                );
    }
}
