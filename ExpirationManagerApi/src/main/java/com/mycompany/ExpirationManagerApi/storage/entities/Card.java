package com.mycompany.ExpirationManagerApi.storage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {
    public enum CardStatus { OK, EXPIRED, ANNULLED }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String cardNumber;

    @Column
    private LocalDate dateOfIssue;

    @Column
    private LocalDate dateOfExpiration;


    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Column
    private CardStatus status;

}
