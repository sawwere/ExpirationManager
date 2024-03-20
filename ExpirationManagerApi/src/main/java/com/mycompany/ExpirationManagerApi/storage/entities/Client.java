package com.mycompany.ExpirationManagerApi.storage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с сущностями типа КЛИЕНТ.
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    /**
     * Идентификатор клиента
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    private Long id;

    /**
     * Имя клиента. Обязательное поле.
     */
    @Column(nullable = false)
    @NotBlank
    private String firstName;

    /**
     * Фамилия клиента. Обязательное поле.
     */
    @Column(nullable = false)
    @NotBlank
    private String lastName;

    /**
     * Отчество клиента. Обязательное поле.
     */
    @Builder.Default
    @Column(nullable = false)
    private String patronymicName = "";

    /**
     * Паспортные данные клиента. Обязательное поле.
     * Строка должна иметь уникальное для всего столбца таблицы значение.
     */
    @Column(unique = true, nullable = false)
    @NotBlank
    private String passport;

    /**
     * Адрес электронной почты клиента. Обязательное поле.
     * Строка должна иметь уникальное для всего столбца таблицы значение.
     */
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    /**
     * Дата рождения клиента. Обязательное поле.
     */
    @Column(nullable = false)
    private LocalDate birthday;

    /**
     * Отражение связи один ко многим с сущностями типа БАНКОВСКАЯ КАРТА.
     */
    @Builder.Default
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Card> cardList  = new ArrayList<Card>();
}
