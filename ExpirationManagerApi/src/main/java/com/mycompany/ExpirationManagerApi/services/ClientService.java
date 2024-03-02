package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.exceptions.AlreadyExistsException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ClientService {
    private static final Logger logger =
            Logger.getLogger(ClientService.class.getName());

    private final ClientRepository clientRepository;

    @Transactional
    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client findClientOrElseThrowException(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id '%s' doesn't exist", clientId))
                );
    }

    @Transactional
    public Client createClient(@Valid ClientDto clientDto) {
        var optionalClient = clientRepository.findFirstByPassportOrEmail(clientDto.getPassport(), clientDto.getEmail());
        if (optionalClient.isPresent()) {
            throw new AlreadyExistsException(
                    String.format("Client with given info already exists with id '%d'",
                            optionalClient.get().getId()));
        }
        Client client = Client.builder()
                .passport(clientDto.getPassport())
                .email(clientDto.getEmail())
                .firstName(clientDto.getFirstName().toUpperCase())
                .lastName(clientDto.getLastName().toUpperCase())
                .patronymicName(clientDto.getPatronymicName().toUpperCase())
                .birthday(clientDto.getBirthday())
                .build();
        clientRepository.save(client);
        logger.info("Created client with id '%d'".formatted(client.getId()));
        return client;
    }

    @Transactional
    public Client updateClient(Long clientId,
                             Optional<String> passport,
                             Optional<String> email,
                             Optional<String> firstName,
                             Optional<String> lastName,
                             Optional<String> patronymicName,
                             Optional<LocalDate> birthdate) {
        Client client = findClientOrElseThrowException(clientId);
        client = Client.builder()
                .passport(passport.orElse(client.getPassport()))
                .email(email.orElse(client.getEmail()))
                .firstName(firstName.orElse(client.getFirstName()))
                .lastName(lastName.orElse(client.getLastName()))
                .patronymicName(patronymicName.orElse(client.getPatronymicName()))
                .birthday(birthdate.orElse(client.getBirthday()))
                .build();
        clientRepository.save(client);
        return client;
    }

    @Transactional
    public void deleteClient(Long clientId) {
        Client client = findClientOrElseThrowException(clientId);
        clientRepository.deleteById(clientId);
        logger.info("Deleted client with id '%d'".formatted(clientId));
    }

    @Transactional
    public List<Client> findAll() {
        return clientRepository.streamAllBy().toList();
    }
}
