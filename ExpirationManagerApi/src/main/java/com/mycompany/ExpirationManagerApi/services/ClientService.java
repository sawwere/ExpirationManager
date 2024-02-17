package com.mycompany.ExpirationManagerApi.services;

import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import com.mycompany.ExpirationManagerApi.storage.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
