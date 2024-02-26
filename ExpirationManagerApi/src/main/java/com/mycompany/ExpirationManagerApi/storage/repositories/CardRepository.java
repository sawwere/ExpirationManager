package com.mycompany.ExpirationManagerApi.storage.repositories;

import com.mycompany.ExpirationManagerApi.storage.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);

    List<Card> findAllBy();
    Stream<Card> streamAllBy();

    @Query(""" 
                SELECT c FROM Card c
                JOIN FETCH c.client
                WHERE c.status = 'OK'
                    AND c.dateOfExpiration - :date = :daysUntilExpiration""")
    List<Card> findAllCloseToExpirationByDuration(LocalDate date, Duration daysUntilExpiration);
}
