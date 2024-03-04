package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.exceptions.ConstraintViolation;
import com.mycompany.ExpirationManagerApi.exceptions.ValidationException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
        validatePassportAndEmailAreUnique(clientDto);
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
    public void deleteClient(Long clientId) {
        Client client = findClientOrElseThrowException(clientId);
        clientRepository.deleteById(clientId);
        logger.info("Deleted client with id '%d'".formatted(clientId));
    }

    @Transactional
    public List<Client> findAll() {
        return clientRepository.streamAllBy().toList();
    }

    private void validatePassportAndEmailAreUnique(ClientDto clientDto) {
        var optionalClient = clientRepository.findFirstByPassportOrEmail(clientDto.getPassport(), clientDto.getEmail());
        if (optionalClient.isPresent()) {
            ArrayList<ConstraintViolation> cvList = new ArrayList<>(2);
            if (clientDto.getPassport().equals(optionalClient.get().getPassport()))
            {
                cvList.add(ConstraintViolation.builder()
                        .field("passport")
                        .message("Passport must be a unique value")
                        .build());

            }
            if (clientDto.getEmail().equals(optionalClient.get().getEmail()))
            {
                cvList.add(ConstraintViolation.builder()
                        .field("email")
                        .message("Email must be a unique value")
                        .build());
            }
            throw new ValidationException(
                    String.format("Client with given info already exists with id '%d'",
                            optionalClient.get().getId()),
                    cvList
            );
        }
    }
}
