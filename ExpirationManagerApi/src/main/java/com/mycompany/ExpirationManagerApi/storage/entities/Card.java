package com.mycompany.ExpirationManagerApi.storage.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    public enum CardStatus { OK, EXPIRED, ANNULLED }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private LocalDate dateOfIssue;

    @Column
    private LocalDate dateOfExpiration;

    @ManyToOne
    private Client client;

    @Column
    private CardStatus status;

}
