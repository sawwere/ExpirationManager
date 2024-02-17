package com.mycompany.ExpirationManagerApi.storage.repositories;

import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
