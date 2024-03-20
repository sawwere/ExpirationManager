package com.mycompany.ExpirationManagerApi.storage.entities;

import com.mycompany.ExpirationManagerApi.storage.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

/**
 * Класс для работы с сущностями типа БАНКОВСКАЯ КАРТА.
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {
    /**
     * Идентификатор карты
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_seq")
    private Long id;

    /**
     * Номер карты. Обязательное поле.
     * Строка, состоящая из 16 десятиных цифр, должна быть уникальной для таблицы.
     */
    @Column(unique = true, nullable = false)
    private String cardNumber;

    /**
     * Дата выпуска карты. Обязательное поле.
     * Должна быть не позже даты истечения срока.
     */
    @Column(nullable = false)
    private LocalDate dateOfIssue;

    /**
     * Дата истечения срока карты. Обязательное поле.
     * Должна быть не раньше даты выпуска.
     */
    @Column(nullable = false)
    private LocalDate dateOfExpiration;

    /**
     * Отражение связи многие к одному с сущностями типа КЛИЕНТ
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    /**
     * Статус карты. Обязательное поле.
     */
    @Column(nullable = false)
    @Enumerated(STRING)
    private CardStatus status;

}
