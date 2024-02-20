package com.mycompany.ExpirationManagerApi.factories;

import com.mycompany.ExpirationManagerApi.dto.ClientDto;
import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoFactory {
    public ClientDto make(Client client) {
        return  ClientDto.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .patronymicName(client.getPatronymicName())
                .passport(client.getPassport())
                .email(client.getEmail())
                .birthday(client.getBirthday())
                .build();
    }
}
