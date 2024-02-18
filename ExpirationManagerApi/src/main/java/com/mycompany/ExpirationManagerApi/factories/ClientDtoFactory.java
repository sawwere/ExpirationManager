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
                .passport(client.getPassport())
                .birthday(client.getBirthday())
                .build();
    }
}
