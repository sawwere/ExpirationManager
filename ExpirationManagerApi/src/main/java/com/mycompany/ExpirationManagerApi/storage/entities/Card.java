package com.mycompany.ExpirationManagerApi.storage.entities;

import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_seq")
    private Long id;

    @Column(unique = true, nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate dateOfIssue;

    @Column(nullable = false)
    private LocalDate dateOfExpiration;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Column(nullable = false)
    @Enumerated(STRING)
    private CardStatus status;

}
