package com.mycompany.ExpirationManagerApi.storage.repositories;

import com.mycompany.ExpirationManagerApi.storage.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.stream.Stream;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByPassport(String passport);
    Optional<Client> findFirstByPassportOrEmail(String passport, String Email);
    Stream<Client> streamAllBy();
}
