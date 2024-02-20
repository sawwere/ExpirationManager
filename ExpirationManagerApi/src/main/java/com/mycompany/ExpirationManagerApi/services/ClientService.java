package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.exceptions.CustomException;
import com.mycompany.ExpirationManagerApi.exceptions.NotFoundException;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }

    public Client findClientOrElseThrowException(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id '%s' doesn't exist", clientId))
                );
    }

    public Client createClient(ClientDto clientDto) {
        var optionalClient = clientRepository.findByPassportOrEmail(clientDto.getPassport(), clientDto.getEmail());
        if (optionalClient.isPresent()) {
            throw new CustomException(
                    String.format("Client with given info already exists with id %d",
                            optionalClient.get().getId()));
        }
        Client client = Client.builder()
                .passport(clientDto.getPassport())
                .email(clientDto.getEmail())
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .patronymicName(clientDto.getPatronymicName())
                .birthday(clientDto.getBirthday())
                .build();
        clientRepository.save(client);
        return client;
    }

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

    public void deleteClient(Long clientId) {
        Client client = findClientOrElseThrowException(clientId);
        clientRepository.deleteById(clientId);
    }

    public Stream<Client> findAll() {
        return clientRepository.streamAllBy();
    }
}
